package controllers.order;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import org.apache.commons.collections.CollectionUtils;

import play.Configuration;
import play.Logger;
import play.Play;
import play.data.DynamicForm;
import play.data.Form;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http.Context;
import play.mvc.Result;
import play.mvc.Security.Authenticated;
import play.twirl.api.Html;
import services.IWebsiteService;
import services.base.FoundationService;
import services.base.utils.MetaUtils;
import services.base.utils.StringUtils;
import services.base.utils.Utils;
import services.member.address.AddressService;
import services.member.login.LoginService;
import services.order.BillDetailService;
import services.order.IOrderEnquiryService;
import services.order.IOrderService;
import services.order.IOrderStatusService;
import services.order.OrderCompositeEnquiry;
import services.order.OrderCompositeRenderer;
import services.order.OrderContextUtil;
import services.order.OrderPaymentService;
import services.order.OrderTaggingService;
import services.order.OrderUpdateService;
import services.order.ProductToOrderService;
import services.order.TotalOrderService;
import services.order.dropShipping.DropShippingMapEnquiryService;
import services.order.exception.OrderException;
import services.order.fragment.renderer.ReplaceOrderDetailRenderer;
import services.order.member.OrderDetailDisplay;
import services.order.payment.OrderPaymentFragmentRenderer;
import services.payment.IPaymentService;
import services.product.ProductAdvertisingCompositeEnquiry;
import valueobjects.base.LoginContext;
import valueobjects.order_api.ExistingOrderComposite;
import valueobjects.order_api.ExistingOrderContext;
import valueobjects.order_api.OrderComposite;
import valueobjects.order_api.OrderConfirmationRequest;
import valueobjects.order_api.OrderContext;
import valueobjects.order_api.OrderValue;
import valueobjects.order_api.payment.PaymentContext;
import valueobjects.payment.LoyaltyForJson;
import valueobjects.product.AdItem;
import valueobjects.product.ProductAdertisingContext;
import authenticators.member.MemberLoginAuthenticator;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.base.Optional;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Maps;
import com.google.common.eventbus.EventBus;

import dto.member.MemberAddress;
import dto.order.BillDetail;
import dto.order.Order;
import dto.order.OrderDetail;
import dto.order.dropShipping.DropShipping;
import events.order.PaymentConfirmationEvent;
import extensions.order.IOrderSourceProvider;
import extensions.payment.IPaymentHTMLPlugIn;
import extensions.payment.IPaymentProvider;
import forms.order.ExistingOrderForm;
import forms.order.PlaceOrder;
import forms.order.ReplaceOrder;

public class OrderProcessing extends Controller {
	@Inject
	private OrderCompositeEnquiry compositeEnquiry;
	@Inject
	private OrderCompositeRenderer compositeRenderer;
	@Inject
	private OrderContextUtil contextUtil;
	@Inject
	private IOrderService orderService;
	@Inject
	private IPaymentService paymentService;
	@Inject
	private Set<IOrderSourceProvider> sourceProviders;
	@Inject
	private IOrderEnquiryService orderEnquiryService;
	@Inject
	private LoginService loginService;
	@Inject
	private OrderPaymentFragmentRenderer paymentRenderer;
	@Inject
	private IOrderStatusService statusService;
	@Inject
	private OrderUpdateService updateService;
	@Inject
	private ReplaceOrderDetailRenderer detailRenderer;
	@Inject
	private OrderPaymentService orderPaymentService;
	@Inject
	private FoundationService foundation;
	@Inject
	private AddressService addressService;
	@Inject
	private OrderUpdateService orderUpdate;
	@Inject
	private ProductToOrderService toOrderService;
	@Inject
	private OrderTaggingService taggingService;
	@Inject
	ProductAdvertisingCompositeEnquiry productAdvertisingService;
	@Inject
	private DropShippingMapEnquiryService dropShippingMapEnquiry;
	@Inject
	private TotalOrderService totalOrderService;

	@Inject
	IOrderEnquiryService enquiryService;

	@Inject
	IWebsiteService websiteService;

	@Inject
	EventBus eventBus;
	@Inject
	private OrderDetailDisplay display;
	@Inject
	BillDetailService billDetailService;

	@Authenticated(MemberLoginAuthenticator.class)
	public Result confirm() {
		OrderContext context = contextUtil.createContext();
		if (null == context) {
			Logger.debug("OrderContext is null");
			return redirect(controllers.base.routes.Home.home());
		}
		if (!context.getCart().checkInventory()) {
			Logger.info("The inventory of cartItem in cart: {}, is not enough",
					context.getCart().getId());
			return badRequest();
		}
		// 判断是否来自登陆
		Map<String, String[]> req = request().queryString();
		context.setIsFromLogin(req.containsKey("isfromlogin"));
		MetaUtils.currentMetaBuilder().setTitle("Checkout");
		OrderComposite vo = compositeEnquiry.getOrderComposite(context);
		Configuration config = Play.application().configuration()
				.getConfig("order");
		if (config != null) {
			String processing = config.getString("processing");
			if ("new".equals(processing)) {
				return ok(views.html.order.preparatory_onepage.render(vo,
						compositeRenderer));
			}
		}
		return ok(views.html.order.onepage.render(vo, compositeRenderer));
	}

	@Authenticated(MemberLoginAuthenticator.class)
	public Result placeOrder() {
		Form<PlaceOrder> orderForm = Form.form(PlaceOrder.class)
				.bindFromRequest();
		if (orderForm.hasErrors()) {
			// XXX handle form error!
			return badRequest("form error: " + orderForm.errorsAsJson());
		}

		Optional<String> source = FluentIterable.from(sourceProviders)
				.transform(sp -> sp.getSource(Context.current()))
				.filter(x -> x != null).first();
		String origin = source.orNull();
		String ip = request().remoteAddress();

		PlaceOrder porder = orderForm.get();

		MemberAddress billAD = addressService
				.getDefaultOrderAddress(loginService.getLoginEmail());
		if (billAD != null && billAD.getIcountry() == null) {
			return badRequest("In bill address , country can not be empty");
		}

		try {
			OrderConfirmationRequest oreq = new OrderConfirmationRequest(
					porder.getCartId(), foundation.getSiteID(),
					porder.getAddressId(), porder.getShippingMethodId(),
					origin, porder.getMessage(), ip, foundation.getLanguage(),
					foundation.getCurrency(), foundation.getVhost());
			String corderId = orderService.confirmOrder(oreq);
			return redirect(controllers.order.routes.OrderProcessing
					.replaceOrder(corderId, porder.getBillId()));
		} catch (OrderException e) {
			Logger.debug("PlaceOrder Form: {}", porder);
			Logger.error("Save Error!", e);
			flash("error", e.getExceptionType().toString());
			return redirect(controllers.order.routes.OrderProcessing.confirm());
		}
	}

	@Authenticated(MemberLoginAuthenticator.class)
	public Result paymentToGo(String orderId) {
		PaymentContext paymentContext = orderService.getPaymentContext(orderId,
				foundation.getLanguage());
		Order order = paymentContext.getOrder().getOrder();
		if (paymentService.getPaymentById(order.getCpaymentid())
				.isNeedExtraInfo()) {
			Map<String, String> map = orderPaymentService.getForm(
					order.getCordernumber(), order.getCpaymentid());
			paymentContext.setMap(map);
		}
		if (order == null
				|| !order.getCmemberemail().equals(
						loginService.getLoginData().getEmail())
				&& statusService
						.getIdByName(IOrderStatusService.PAYMENT_PENDING) == order
						.getIstatus()) {
			return badRequest();
		}
		String paymentId = order.getCpaymentid();
		IPaymentProvider paymentProvider = paymentService
				.getPaymentById(paymentId);
		List<IPaymentHTMLPlugIn> plugIns = paymentService
				.getHTMLPlugIns(IPaymentHTMLPlugIn.WAIT_PAY);
		return ok(views.html.order.payment.do_payment.render(paymentContext,
				paymentProvider, plugIns));
	}

	public Result paymentConfirmed(String orderId) {
		return this.orderCompleted(orderId);
		// Order order = orderEnquiryService.getOrderById(orderId);
		// if (order == null) {
		// return badRequest();
		// }
		// // add by lijun
		// LoginContext loginCtx = this.foundation.getLoginContext();
		// boolean isLogin = loginCtx.isLogin();
		//
		// List<IPaymentHTMLPlugIn> plugIns = paymentService
		// .getHTMLPlugIns(IPaymentHTMLPlugIn.PAY_SUCCESS);
		// return ok(views.html.payment.paySuccess.render(order, plugIns,
		// isLogin));
	}

	public Result paymentFail(String orderId, String errorMessage,
			String errorCode) {
		// modify by lijun
		// Order order = orderEnquiryService.getOrderById(orderId);
		return ok(views.html.payment.payFail.render(orderId, errorMessage,
				errorCode, null));
	}

	/**
	 * paypal支付失败view
	 * 
	 * @author lijun
	 * @param orderId
	 * @param errorMessage
	 * @param errorCode
	 * @param retryUrl
	 * @return
	 */
	public Result paymentFailed(String orderId, String errorMessage,
			String errorCode, String retryUrl) {
		// modify by lijun
		// Order order = orderEnquiryService.getOrderById(orderId);
		return ok(views.html.payment.payFail.render(orderId, errorMessage,
				errorCode, retryUrl));
	}

	/**
	 * @author liudi 订单完成页面
	 */
	public Result orderCompleted(String ordernumber) {
		// meta handling
		MetaUtils.currentMetaBuilder().setTitle("Order Completed--TOMTOP")
				.setDescription("Order Completed--TOMTOP")
				.addKeyword("Order Completed--TOMTOP");

		// 获取广告
		ProductAdertisingContext pac = new ProductAdertisingContext(null, 4,
				foundation.getSiteID(), foundation.getLanguage(), 1,
				foundation.getDevice());
		List<AdItem> advertisingList = productAdvertisingService
				.getAdvertisings(pac);
		LoginContext loginCtx = this.foundation.getLoginContext();
		Boolean isLogin = loginCtx.isLogin();

		PaymentContext paymentContext = orderService.getPaymentContext(
				ordernumber, foundation.getLanguage());

		Order succorder = null;
		if (paymentContext != null && paymentContext.getOrder() != null) {
			Order order = paymentContext.getOrder().getOrder();
			succorder = order;
		} else {
			ordernumber = null;
		}

		List<IPaymentHTMLPlugIn> plugIns = paymentService
				.getHTMLPlugIns(IPaymentHTMLPlugIn.PAY_SUCCESS);
		List<BillDetail> loyaltys = new ArrayList<BillDetail>();
		if (null != succorder && null != succorder.getIid()) {
			loyaltys = billDetailService.getExtraBill(succorder.getIid());
		}
		List<LoyaltyForJson> loyaltyForJsons = new ArrayList<LoyaltyForJson>();
		if (CollectionUtils.isNotEmpty(loyaltys)) {
			for (int i = 0; i < loyaltys.size(); i++) {
				LoyaltyForJson loyaltyForJson = new LoyaltyForJson(loyaltys
						.get(i).getCtype(), loyaltys.get(i).getCmsg(),
						succorder.getCcurrency(), Utils.money(loyaltys.get(i)
								.getFtotalprice(), succorder.getCcurrency()));
				loyaltyForJsons.add(loyaltyForJson);
			}
		}
		String loyaltysJson = Json.toJson(loyaltyForJsons).toString();
		return ok(views.html.order.payment.order_completed.render(ordernumber,advertisingList, isLogin, plugIns, succorder, loyaltysJson));
	}

	/**
	 * 没有登录的订单详情
	 */
	public Result noLoginOrderDetail(String id) {
		PaymentContext context = orderService.getPaymentContext(id,
				foundation.getLanguage());
		Order order = context.getOrder().getOrder();
		if (order == null) {
			return badRequest();
		}
		Html orderDetail = display.getOrderDetail(context);
		return ok(views.html.order.v2.New_track_order_list.render(orderDetail));
	}

	@Authenticated(MemberLoginAuthenticator.class)
	public Result replaceOrder(String corderId, Integer billID) {
		PaymentContext paymentContext = orderService.getPaymentContext(
				corderId, foundation.getLanguage());
		// modify by lijun for fix NPE
		if (paymentContext == null) {
			return this.paymentFail(corderId, "your order:" + corderId
					+ " is invalid", "");
		}

		paymentContext.setBillID(billID);
		Order order = paymentContext.getOrder().getOrder();
		if (order == null
				|| !order.getCmemberemail().equals(
						loginService.getLoginData().getEmail())
				|| statusService
						.getIdByName(IOrderStatusService.PAYMENT_PENDING) != order
						.getIstatus()) {
			// modify by lijun
			// return internalServerError("Not correct order");
			return this.paymentFail(corderId, "Not correct order", "");
		}
		// 检查订单是否已经支付完成
		boolean isAlreadyPaid = orderService.isAlreadyPaid(corderId);
		if (isAlreadyPaid) {
			return redirect(controllers.order.routes.OrderProcessing
					.paymentFail(corderId,
							"Your order has been paid completed", ""));
		}
		if (order.getFshippingprice() == null) {
			// modify by lijun if shipping price is null then go to EC
			// return
			// ok("This is a bad order,you need feedback to support staff");
			return redirect("/paypal/ec-order?orderNum=" + corderId);
		}
		List<String> tags = taggingService.getOrderTags(order.getIid());
		List<String> payments = paymentService.filterByOrderTag(tags);
		Map<String, Html> htmlMap = Maps.newHashMap();
		htmlMap.put("detail", detailRenderer.render(paymentContext.getOrder()
				.getOrder(), paymentContext.getCurrency()));
		htmlMap.put("payment", paymentRenderer.filterRender(
				order.getCpaymentid(), payments, paymentContext));
		return ok(views.html.order.pay_order.render(paymentContext, htmlMap));
	}

	@Authenticated(MemberLoginAuthenticator.class)
	public Result changePayment() {
		Form<ReplaceOrder> orderForm = Form.form(ReplaceOrder.class)
				.bindFromRequest();
		if (orderForm.hasErrors()) {
			return badRequest("form error: " + orderForm.errorsAsJson());
		}
		ReplaceOrder form = orderForm.get();
		MemberAddress bill = addressService.getDefaultOrderAddress(loginService
				.getLoginEmail());
		if (bill == null || bill.getIcountry() == null) {
			return badRequest("country in bill address can not be empty");
		}
		Order order = orderEnquiryService.getOrderById(form.getCorderId());
		// 检查订单是否已经支付完成
		boolean isAlreadyPaid = orderService.isAlreadyPaid(form.getCorderId());
		if (isAlreadyPaid) {
			return redirect(controllers.order.routes.OrderProcessing
					.paymentFail(form.getCorderId(),
							"Your order has been paid completed", ""));
		}
		if (paymentService.getPaymentById(form.getPaymentId())
				.isNeedExtraInfo()) {
			DynamicForm df = Form.form().bindFromRequest();
			if (!paymentService.getPaymentById(form.getPaymentId()).validForm(
					df)) {
				return badRequest("form error: " + df.errorsAsJson());
			}
			orderPaymentService.createOrderPayment(form.getCorderId(),
					form.getPaymentId(), df);
		}
		Integer status = statusService
				.getIdByName(IOrderStatusService.PAYMENT_PENDING);
		String email = loginService.getLoginData().getEmail();
		boolean isSuccess = updateService.replaceOrder(form, status, email);
		PaymentContext paymentContext = orderService.getPaymentContext(
				form.getCorderId(), foundation.getLanguage());
		order = paymentContext.getOrder().getOrder();
		if (isSuccess) {
			return paymentToGo(order.getCordernumber());
		}
		return internalServerError();
	}

	@Authenticated(MemberLoginAuthenticator.class)
	public Result selectOrder(String orderNumber, Boolean isSelect) {
		ExistingOrderContext context = contextUtil.createExstingOrderContext(
				orderNumber, isSelect);
		if (context == null) {
			Logger.debug("orderNumber: {}, context == null, return badRequest",
					orderNumber);
			return badRequest("Can not found the order");
		}
		MetaUtils.currentMetaBuilder().setTitle("Checkout");
		ExistingOrderComposite vo = compositeEnquiry.getOrderComposite(context);
		return ok(views.html.order.existing_onepage.render(vo,
				compositeRenderer));
	}

	@Authenticated(MemberLoginAuthenticator.class)
	public Result submitOrder() {
		Form<ExistingOrderForm> form = Form.form(ExistingOrderForm.class)
				.bindFromRequest();
		if (form.hasErrors()) {
			return badRequest("form error: " + form.errorsAsJson());
		}
		ExistingOrderForm orderForm = form.get();
		String message = orderForm.getMessage();
		if (message != null && !"".equals(message)) {
			orderUpdate.updateOrderMessage(orderForm.getOrderID(), message);
		}

		if (orderForm.getIsSelect()) {
			if (orderForm.getAddressId() != null) {
				MemberAddress address = addressService
						.getMemberAddressById(orderForm.getAddressId());
				orderUpdate.updateOrderAddress(orderForm.getOrderID(), address);
			}
			toOrderService.updateShippingMethod(orderForm.getOrderID(),
					orderForm.getShippingMethodId(), foundation.getSiteID(),
					foundation.getLanguage());
		}
		PaymentContext paymentContext = orderService.getPaymentContext(
				orderForm.getOrderID(), foundation.getLanguage());

		Order order = paymentContext.getOrder().getOrder();
		if (order == null
				|| !order.getCmemberemail().equals(
						loginService.getLoginData().getEmail())) {
			return internalServerError("Not correct order");
		} else if (statusService
				.getIdByName(IOrderStatusService.PAYMENT_PENDING) != order
				.getIstatus()
				&& null != order.getIstatus()) {
			return internalServerError("Not correct order");
		}
		List<String> tags = taggingService.getOrderTags(order.getIid());
		List<String> payments = paymentService.filterByOrderTag(tags);
		Map<String, Html> htmlMap = Maps.newHashMap();
		htmlMap.put("detail", detailRenderer.render(paymentContext.getOrder()
				.getOrder(), paymentContext.getCurrency()));
		htmlMap.put("payment", paymentRenderer.filterRender(
				order.getCpaymentid(), payments, paymentContext));
		return ok(views.html.order.pay_order.render(paymentContext, htmlMap));
	}

	@Authenticated(MemberLoginAuthenticator.class)
	public Result checkAddress(Integer addressID, Integer billID) {
		Map<String, Object> data = Maps.newHashMap();
		MemberAddress address = addressService.getMemberAddressById(addressID);
		MemberAddress bill = billID != null ? addressService
				.getMemberAddressById(billID) : addressService
				.getDefaultOrderAddress(loginService.getLoginEmail());
		data.put("res", true);
		data.put("msg", "In address , country can not be empty");
		if (address == null || bill == null) {
			data.put("res", false);
		} else if (address != null && address.getIcountry() == null) {
			data.put("res", false);
		} else if (bill != null && bill.getIcountry() == null) {
			data.put("res", false);
		}
		return ok(Json.toJson(data));
	}
	
	/**
	 * 给ipn发事件，通知订单成功，发送优惠券
	 */
	public Result sendOrderCompleteEvent(){
		JsonNode jnode = request().body().asJson();
		if(jnode==null){
			return ok("data is null");
		}
		JsonNode ordernums = jnode.get("orderNums");
		String transactionId = jnode.get("transactionId").asText();
		Iterator<JsonNode> orderarr = ordernums.iterator();
		
		int langId = foundation.getLanguage();
		while(orderarr.hasNext()){
			JsonNode od = orderarr.next();
			String o = od.asText();
			Logger.debug("send completed event ordernum:{}",o);
			if (o.endsWith("-DS")) {
				List<String> orderIDs = dropShippingMapEnquiry
						.getOrderNumbersByID(o);
				for (String id : orderIDs) {
					Order order = enquiryService.getOrderById(id);
					if(order==null){
						continue;
					}
					List<OrderDetail> details = enquiryService.getOrderDetails(order.getIid());
					eventBus.post(new PaymentConfirmationEvent(new OrderValue(order,
							details), transactionId, langId));
				}
			} else if (o.endsWith("-TT")) {
				List<Order> orderTTs = totalOrderService.getOrdersByTotalCID(o);
				for (Order order : orderTTs) {
					List<OrderDetail> details = enquiryService.getOrderDetails(order.getIid());
					eventBus.post(new PaymentConfirmationEvent(new OrderValue(order,
							details), transactionId, langId));
				}
			} else {
				Order order = enquiryService.getOrderById(o);
				if(order==null){
					continue;
				}
				List<OrderDetail> details = enquiryService.getOrderDetails(order.getIid());
				eventBus.post(new PaymentConfirmationEvent(new OrderValue(order,
						details), transactionId, langId));
			}
		}
		return ok("success");
	}
}
