package controllers.order;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import play.data.DynamicForm;
import play.data.DynamicForm.Dynamic;
import play.data.Form;
import play.libs.F.Tuple;
import play.libs.F.Tuple3;
import play.mvc.Controller;
import play.mvc.Http.Context;
import play.mvc.Result;
import play.mvc.Security.Authenticated;
import play.twirl.api.Html;
import services.base.FoundationService;
import services.base.utils.JsonFormatUtils;
import services.base.utils.StringUtils;
import services.member.login.LoginService;
import services.order.IOrderService;
import services.order.IOrderStatusService;
import services.order.OrderPaymentService;
import services.order.OrderSplitService;
import services.order.OrderStatusService;
import services.order.OrderTaggingService;
import services.order.OrderUpdateService;
import services.order.TotalOrderService;
import services.order.fragment.renderer.ReplaceOrderDetailRenderer;
import services.order.payment.OrderPaymentFragmentRenderer;
import services.payment.IPaymentService;
import valueobjects.order_api.OrderItem;
import valueobjects.order_api.OrderSplitRequest;
import valueobjects.order_api.OrderSubmitInfo;
import valueobjects.order_api.payment.PaymentContext;
import authenticators.member.MemberLoginAuthenticator;

import com.google.common.base.Optional;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Maps;

import dto.order.Order;
import enums.OrderLableEnum;
import extensions.order.IOrderSourceProvider;
import extensions.payment.IPaymentHTMLPlugIn;
import extensions.payment.IPaymentProvider;
import forms.order.ReplaceOrder;

@Authenticated(MemberLoginAuthenticator.class)
public class NewOrderProcessing extends Controller {
	@Inject
	private Set<IOrderSourceProvider> sourceProviders;
	@Inject
	private FoundationService foundation;
	@Inject
	private LoginService loginService;
	@Inject
	private OrderSplitService orderSplitService;
	@Inject
	private TotalOrderService totalOrderService;
	@Inject
	private OrderStatusService statusService;
	@Inject
	private OrderTaggingService taggingService;
	@Inject
	private IPaymentService paymentService;
	@Inject
	private ReplaceOrderDetailRenderer detailRenderer;
	@Inject
	private OrderPaymentFragmentRenderer paymentRenderer;
	@Inject
	private OrderPaymentService orderPaymentService;
	@Inject
	private OrderUpdateService updateService;
	@Inject
	IOrderService orderService;

	public Result submitOrderInfo() {
		Dynamic form = Form.form().bindFromRequest().get();
		String data = (String) form.getData().get("data");
		if (StringUtils.isEmpty(data)) {
			return badRequest("Can not found order submit data");
		}
		OrderSubmitInfo info = JsonFormatUtils.jsonToBean(data,
				OrderSubmitInfo.class);
		if (info == null) {
			return badRequest("Order submit info is null");
		}
		Optional<String> source = FluentIterable.from(sourceProviders)
				.transform(sp -> sp.getSource(Context.current()))
				.filter(x -> x != null).first();
		String origin = source.orNull();
		String ip = request().remoteAddress();
		OrderSplitRequest req = new OrderSplitRequest(foundation.getSiteID(),
				origin, ip, foundation.getLanguage(), foundation.getCurrency(),
				foundation.getVhost(), loginService.getLoginData()
						.getMemberId());
		List<Order> orders = orderSplitService.split(info, req);
		if (orders != null && orders.size() == 1) {
			Order order = orders.get(0);
			return redirect(routes.OrderProcessing.replaceOrder(
					order.getCordernumber(), info.getBillAddressID()));
		} else if (orders != null && orders.size() > 1) {
			String totalCID = totalOrderService.createTotalOrder(orders);
			return redirect(routes.NewOrderProcessing.replaceOrder(totalCID,
					info.getBillAddressID()));
		}
		return badRequest();
	}

	public Result replaceOrder(String totalCID, Integer billID) {
		Tuple<PaymentContext, List<OrderItem>> tuple = totalOrderService
				.getPaymentContext(totalCID, foundation.getLanguage());
		PaymentContext paymentContext = tuple._1;
		paymentContext.setBillID(billID);
		Order order = paymentContext.getOrder().getOrder();
		if (order == null
				|| !order.getCmemberemail()
						.equals(loginService.getLoginEmail())
				|| statusService
						.getIdByName(IOrderStatusService.PAYMENT_PENDING) != order
						.getIstatus()) {
			return badRequest("Not correct order");
		}
		if (order.getFshippingprice() == null) {
			return ok("This is a bad order,you need feedback to support staff");
		}
		List<String> tags = taggingService.getOrderTags(order.getIid());
		List<String> payments = paymentService.filterByOrderTag(tags);
		Map<String, Html> htmlMap = Maps.newHashMap();
		htmlMap.put("detail", detailRenderer.newRender(paymentContext
				.getOrder().getOrder(), paymentContext.getCurrency(),
				paymentContext.getOrder().getDetails()));
		htmlMap.put("payment", paymentRenderer.newFilterRender(
				order.getCpaymentid(), payments, paymentContext));
		return ok(views.html.order.payment.new_pay_order.render(paymentContext,
				htmlMap));
	}

	public Result changePayment() {
		Form<ReplaceOrder> orderForm = Form.form(ReplaceOrder.class)
				.bindFromRequest();
		if (orderForm.hasErrors()) {
			return badRequest("form error: " + orderForm.errorsAsJson());
		}
		ReplaceOrder form = orderForm.get();
		Tuple3<PaymentContext, List<OrderItem>, List<Order>> tuple = totalOrderService
				.getPaymentContextAndOrder(form.getCorderId(),
						foundation.getLanguage());
		PaymentContext paymentContext = tuple._1;
		List<Order> orders = tuple._3;
		
		// 检查订单是否已经支付完成
		boolean isAlreadyPaid = orderService.isAlreadyPaid(form.getCorderId());
		if (isAlreadyPaid) {
			return redirect(controllers.order.routes.OrderProcessing
					.paymentFail(form.getCorderId(),
							"Your order has been paid completed", ""));
		}
		
		paymentContext.setOrderLable(OrderLableEnum.TOTAL_ORDER.getName());
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
		for (Order o : orders) {
			ReplaceOrder tForm = new ReplaceOrder();
			tForm.setCorderId(o.getCordernumber());
			tForm.setOrderId(o.getIid());
			tForm.setPaymentId(form.getPaymentId());
			updateService.replaceOrder(tForm, status, email);
		}
		return paymentToGo(paymentContext, form);
	}

	private Result paymentToGo(PaymentContext paymentContext, ReplaceOrder form) {
		Order order = paymentContext.getOrder().getOrder();
		order.setCpaymentid(form.getPaymentId());
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
}
