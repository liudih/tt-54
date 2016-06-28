package controllers.order;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.common.collect.Lists;

import play.Logger;
import play.Play;
import play.data.DynamicForm;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Http.Context;
import play.mvc.Result;
import services.ICountryService;
import services.ICurrencyService;
import services.base.FoundationService;
import services.base.utils.CookieUtils;
import services.base.utils.MetaUtils;
import services.cart.ICartServices;
import services.member.address.IAddressService;
import services.member.login.ILoginService;
import services.order.ICheckoutService;
import services.order.IOrderEnquiryService;
import services.order.IOrderService;
import services.order.IOrderStatusService;
import services.order.OrderCompositeEnquiry;
import services.order.OrderCompositeRenderer;
import services.order.OrderPaymentService;
import services.order.OrderService;
import services.order.OrderUpdateService;
import services.order.payment.OrderPaymentFragmentRenderer;
import services.payment.IPaymentService;
import services.shipping.IShippingServices;
import valueobjects.base.LoginContext;
import valueobjects.cart.BundleCartItem;
import valueobjects.cart.CartItem;
import valueobjects.cart.SingleCartItem;
import valueobjects.loyalty.LoyaltyPrefer;
import valueobjects.order_api.CreateOrderRequest;
import valueobjects.order_api.OrderComposite;
import valueobjects.order_api.OrderContext;
import valueobjects.order_api.payment.PaymentContext;

import com.google.common.base.Optional;
import com.google.common.collect.Collections2;
import com.google.common.collect.FluentIterable;
import com.google.inject.Inject;

import context.ContextUtils;
import context.WebContext;
import dto.Country;
import dto.Currency;
import dto.member.MemberAddress;
import dto.order.Order;
import extensions.order.IOrderSourceProvider;
import extensions.order.IPreferProvider;
import forms.order.PlaceOrderV2;
import forms.order.PlaceOrderV3;
import services.IStorageService;

/**
 * 新版订单流程
 * 
 * @author lijun
 *
 */
public class OrderController extends Controller {

	@Inject
	FoundationService foundation;

	@Inject
	IAddressService addressService;

	@Inject
	ICountryService countryService;

	@Inject
	OrderCompositeEnquiry productRender;

	@Inject
	IOrderService orderService;

	@Inject
	ICartServices cartService;

	@Inject
	OrderCompositeEnquiry compositeEnquiry;

	@Inject
	OrderCompositeRenderer compositeRenderer;

	@Inject
	OrderPaymentFragmentRenderer paymentRenderer;

	@Inject
	ICurrencyService currencyService;

	@Inject
	IOrderEnquiryService orderEnquiryService;

	@Inject
	ILoginService loginService;

	@Inject
	IPaymentService paymentService;

	@Inject
	IOrderStatusService statusService;

	@Inject
	OrderUpdateService updateService;

	@Inject
	OrderPaymentService orderPaymentService;

	@Inject
	IPreferProvider prefer;

	@Inject
	Set<IOrderSourceProvider> sourceProviders;

	// add by lijun
	@Inject
	IShippingServices shippingServices;

	@Inject
	ICheckoutService checkoutService;

	@Inject
	IStorageService iStorageService;
	
	/**
	 * 会员通道
	 */
	public Result checkoutForMemberStep1(int storageid) {
		MetaUtils.currentMetaBuilder().setTitle("Checkout");
		LoginContext loginCtx = foundation.getLoginContext();
		if (!loginCtx.isLogin()) {
//			return badRequest("not logged in");
			return redirect(controllers.member.routes.Login
					.loginForm(null));
		}
		String email = loginCtx.getMemberID();
		//保存选择的仓库id
		CookieUtils.setCookie("storageid", storageid+"", Context.current());

		// 查看用户是否已经填写过ship地址
		Integer shipCount = addressService
				.getShippingAddressCountByEmail(email);

		if (shipCount == null || shipCount == 0) {
			List<Country> countries = countryService.getAllCountries();
			String nextStepUrl = controllers.order.routes.OrderController
					.checkoutForMemberStep2(storageid).absoluteURL(
							Context.current().request());
			return ok(views.html.cart.v2.checkout_step1_new_member.render(
					countries, nextStepUrl));
		}
		return this.checkoutForMemberStep2(storageid);
	}

	public Result checkoutForMemberStep2(int storageid) {
		//仓库id
		MetaUtils.currentMetaBuilder().setTitle("Order Confirm");
		LoginContext loginCtx = foundation.getLoginContext();
		boolean isLogin = loginCtx.isLogin();
		if (!isLogin) {
			return redirect(controllers.member.routes.Login
					.loginForm(null));
//			return badRequest("not logged in");
		}

		int site = this.foundation.getSiteID();
		int lang = this.foundation.getLanguage();
		String currencyCode = this.foundation.getCurrency();

		List<CartItem> items = cartService
				.getAllItems(site, lang, currencyCode);
		String cartUrl = Play.application().configuration()
				.getString("cart.url");
		if (items == null || items.size() == 0) {
			return redirect(cartUrl);
		}
		//过滤仓库id
		items = Lists.newArrayList(Collections2.filter(items, c -> c.getStorageID()==storageid));
		if(items.size()==0){
			return redirect(cartUrl);
		}

		String email = loginCtx.getMemberID();

		List<MemberAddress> shipAddress = addressService
				.getMemberShippingAddressByEmail(email);

		FluentIterable.from(shipAddress).forEach(
				a -> {
					if (a.getIcountry() != null) {
						Country country = countryService
								.getCountryByCountryId(a.getIcountry());
						a.setCountryFullName(country.getCname());
						a.setCountryCode(country.getCshortname());
					}
				});

		List<Country> countries = countryService.getAllCountries();

		Currency currency = currencyService.getCurrencyByCode(currencyCode);

		Order order = new Order();

		List<CartItem> allItems = Lists.newLinkedList();
		double subTotal = checkoutService.subToatl(items);
		for (CartItem item : items) {
			if (item instanceof SingleCartItem) {
				allItems.add(item);
			} else if (item instanceof BundleCartItem) {
				List<SingleCartItem> childs = ((BundleCartItem) item)
						.getChildList();
				allItems.addAll(childs);
			}
		}
		order.setFordersubtotal(subTotal);

		order.setFshippingprice(0.0);

		Context httpCtx = Context.current();
		WebContext webCtx = ContextUtils.getWebContext(httpCtx);

		List<LoyaltyPrefer> prefers = prefer.getAllPreferByEmail(email, items,
				webCtx);

		double discount = 0.0;
		if (prefers != null) {
			for (LoyaltyPrefer p : prefers) {
				discount = discount + p.getValue();
			}
		}

		order.setFextra(discount);

		order.setFgrandtotal(subTotal + discount);

		OrderContext orderCtx = new OrderContext(email, allItems);
		LinkedList<String> pluginNames = Lists.newLinkedList();
		pluginNames.add("cart-product");
		pluginNames.add("payment");

		OrderComposite vo = compositeEnquiry.getOrderComposite(orderCtx,
				pluginNames);

		return ok(views.html.cart.v2.checkout_step2_new_member.render(
				shipAddress, isLogin, countries, vo, compositeRenderer,
				paymentRenderer, order, currency, prefers, storageid));
	}

	public Result placeOrder() {

		LoginContext loginCtx = foundation.getLoginContext();
		boolean isLogin = loginCtx.isLogin();
		if (!isLogin) {
			return redirect(controllers.member.routes.Login
					.loginForm(null));
//			return badRequest("not logged in");
		}
		String email = loginCtx.getMemberID();

		int site = this.foundation.getSiteID();
		int lang = this.foundation.getLanguage();
		String currencyCode = this.foundation.getCurrency();
		List<CartItem> items = cartService
				.getAllItems(site, lang, currencyCode);
		String cartUrl = Play.application().configuration()
				.getString("cart.url");
		//获取仓库id
		DynamicForm df = Form.form().bindFromRequest();
		String storageid = df.get("storageid");
		 
		
		storageid = (storageid==null||"".equals(storageid)) ? "1" : storageid;
		Logger.debug("get===storageid==={}",storageid);
		if (items == null || items.size() == 0 || !StringUtils.isNumeric(storageid)) {
			return redirect(cartUrl);
		}
		//过滤仓库id
		final int intstorageid = Integer.parseInt(storageid);
		items = Lists.newArrayList(Collections2.filter(items, c -> c.getStorageID()==intstorageid));
		if(items.size()==0){
			Logger.debug("filter items after is empty");
			return redirect(cartUrl);
		}

		// 判断所有商品是否是同一个仓库
		Integer firstStorage = items.get(0).getStorageID();
		if (firstStorage == null) {
			Logger.debug("storage id is null in cart");
			return badRequest();
		}
		
		//~ 获取真实仓库
		int tstorid = firstStorage;
		List<dto.Storage> storagelist = iStorageService.getAllStorages();
		List<dto.Storage> newstoragelist = Lists.newArrayList(Collections2
				.filter(storagelist, c -> c.getIparentstorage() == tstorid));
		if (newstoragelist != null && newstoragelist.size() > 0) {
			firstStorage = newstoragelist.get(0).getIid();
		}
		
		List<String> listingId = Lists.newLinkedList();
		FluentIterable
				.from(items)
				.forEach(
						item -> {
							if (item instanceof valueobjects.cart.SingleCartItem) {
								listingId.add(item.getClistingid());
							} else if (item instanceof valueobjects.cart.BundleCartItem) {
								List<valueobjects.cart.SingleCartItem> childs = ((valueobjects.cart.BundleCartItem) item)
										.getChildList();
								List<String> clisting = Lists.transform(childs,
										c -> c.getClistingid());
								listingId.addAll(clisting);
							}
						});

/*	   启用多仓库就不用这个检查了
       boolean isSameStorage = shippingServices.isSameStorage(listingId,
				firstStorage.toString());
		if (!isSameStorage) {
			Logger.debug("storage do not same when place order");
			return badRequest("storage do not same when place order");
		}*/
		Form<PlaceOrderV3> orderForm = Form.form(PlaceOrderV3.class)
				.bindFromRequest();
		if (orderForm.hasErrors()) {
			Logger.debug("form has errors when placeOrder");
			return badRequest("form error: " + orderForm.errorsAsJson());
		}

		PlaceOrderV3 form = orderForm.get();

		Integer addressId = form.getAddressId();

		Optional<String> source = FluentIterable.from(sourceProviders)
				.transform(sp -> sp.getSource(Context.current()))
				.filter(x -> x != null).first();
		String origin = source.orNull();

		String message = form.getMessage();

		String ip = this.foundation.getClientIP();
		String currency = this.foundation.getCurrency();
		String vhost = this.foundation.getVhost();

		Context httpCtx = Context.current();
		WebContext webCtx = ContextUtils.getWebContext(httpCtx);

		List<LoyaltyPrefer> prefers = prefer.getAllPreferByEmail(email, items,
				webCtx);

		//物流方式的code
		String shipMethodCode = df.get("shipMethodCode");
		CreateOrderRequest request = new CreateOrderRequest(items, site,
				addressId, null, origin, message, ip, lang,
				currency, vhost, prefers, firstStorage);
		request.setCpaymenttype(form.getPaymentId());
		
		request.setShipMethodId(form.getShipMethodId());
		request.setShipCode(shipMethodCode);

		Order order = this.orderService.createOrderInstance(request);
		if (order.getIid() != null) {
			cartService.deleteItem(items);
			if (httpCtx.request().cookie("loyalty") != null) {
				CookieUtils.removeCookie("loyalty", httpCtx);
			}
			// 将订单对象传入优惠信息中
			if (null != prefers && prefers.size() > 0) {
				for (int i = 0; i < prefers.size(); i++) {
					prefers.get(i).setOrder(order);
				}
			}
			prefer.saveAllPrefer(email, prefers, webCtx);
		}
		// 如果用户使用优惠后导致付款金额为0或负值时,不能去付款
		Double grandtotal = order.getFgrandtotal();
		if (grandtotal == null || grandtotal <= 0) {
			return redirect(controllers.order.routes.OrderProcessing
					.paymentFailed(order.getCordernumber(),
							"Order total is invalid.", "", ""));
		}
		String paymentId = form.getPaymentId();
		String orderNum = order.getCordernumber();
		if ("paypal".equals(paymentId)) {
			return redirect("/paypal/ec?ordernum=" + orderNum);
		} else {
			MemberAddress bill = addressService
					.getDefaultOrderAddress(loginService.getLoginEmail());
			if (("oceanpayment_credit".equals(paymentId) || "oceanpayment_jcb".equals(paymentId) )
					&& (bill == null || bill.getIcountry() == null)) {
				return badRequest("country in bill address can not be empty");
			}
			if (paymentService.getPaymentById(form.getPaymentId())
					.isNeedExtraInfo()) {
				
				if (!paymentService.getPaymentById(form.getPaymentId())
						.validForm(df)) {
					return badRequest("form error: " + df.errorsAsJson());
				}
				orderPaymentService.createOrderPayment(orderNum,
						form.getPaymentId(), df);
			}
			Integer status = statusService
					.getIdByName(IOrderStatusService.PAYMENT_PENDING);
			boolean isSuccess = updateService.replaceOrder(order.getIid(),
					status, paymentId, email);
			PaymentContext paymentContext = orderService.getPaymentContext(
					orderNum, foundation.getLanguage());
			order = paymentContext.getOrder().getOrder();
			if (isSuccess) {

				String redirectUrl = controllers.order.routes.OrderProcessing
						.paymentToGo(orderNum).absoluteURL(
								Context.current().request());
				return redirect(redirectUrl);
			}
			return internalServerError();
		}

	}
}
