package controllers.paypal;

import java.util.List;
import java.util.Map;
import java.util.Set;

import mapper.order.OrderMapper;

import org.elasticsearch.common.collect.Lists;

import play.Logger;
import play.Play;
import play.data.Form;
import play.data.validation.ValidationError;
import play.mvc.Controller;
import play.mvc.Http.Context;
import play.mvc.Http.Cookie;
import play.mvc.Result;
import play.twirl.api.Html;
import services.ICountryService;
import services.ICurrencyService;
import services.base.FoundationService;
import services.order.IFreightService;
import services.order.IOrderService;
import services.order.OrderCompositeEnquiry;
import services.order.OrderCompositeRenderer;
import services.order.fragment.provider.OrderShippingMethodProvider;
import services.order.fragment.renderer.OrderCartProductRenderer;
import services.order.fragment.renderer.OrderShippingMethodRenderer;
import services.paypal.IExpressCheckoutNvpService;
import services.shipping.IShippingServices;
import valueobjects.base.LoginContext;
import valueobjects.cart.CartItem;
import valueobjects.loyalty.LoyaltyPrefer;
import valueobjects.order_api.CreateOrderRequest;
import valueobjects.order_api.ExistingOrderComposite;
import valueobjects.order_api.ExistingOrderContext;
import valueobjects.order_api.IOrderFragment;
import valueobjects.order_api.cart.SingleCartItem;
import valueobjects.order_api.payment.PaymentContext;
import valueobjects.paypal_api.PaypalNvpPaymentStatus;
import valueobjects.paypal_api.SetExpressCheckout;

import com.google.common.base.Optional;
import com.google.common.collect.Collections2;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Maps;
import com.google.inject.Inject;

import context.ContextUtils;
import context.WebContext;
import dao.order.IOrderDetailEnquiryDao;
import dto.Country;
import dto.Currency;
import dto.member.MemberAddress;
import dto.order.Order;
import extensions.order.IOrderSourceProvider;
import extensions.order.IPreferProvider;
import forms.order.OrderConfirmForm;
import services.base.utils.CookieUtils;
import services.base.utils.MetaUtils;
import services.base.utils.Utils;
import services.cart.ICartServices;
import services.IStorageService;

/**
 * nvp方式和paypal通信
 * 
 * @author lijun
 *
 */

public class ExpressCheckoutNvp extends Controller {

	@Inject
	IExpressCheckoutNvpService service;

	@Inject
	FoundationService foundation;

	@Inject
	IOrderService orderService;

	@Inject
	OrderShippingMethodProvider shipMethod;

	@Inject
	OrderShippingMethodRenderer shipMethodRenderer;

	@Inject
	ICurrencyService currencyService;

	@Inject
	ICountryService countryService;

	@Inject
	OrderCompositeEnquiry plugin;

	@Inject
	OrderCompositeEnquiry productRender;

	@Inject
	OrderCompositeRenderer compositeRenderer;

	@Inject
	OrderMapper orderMapper;

	@Inject
	ICartServices cartService;

	@Inject
	Set<IOrderSourceProvider> sourceProviders;

	@Inject
	IShippingServices shippingServices;

	@Inject
	IPreferProvider prefer;

	@Inject
	IOrderDetailEnquiryDao orderDetailDao;

	@Inject
	private IFreightService freightService;
	
	@Inject
	IStorageService iStorageService;

	/**
	 * 用户未登陆的情况下快捷支付
	 * 
	 * @author lijun
	 * @param ordernum
	 * @return
	 */
	public Result setExpressCheckoutForCart(int storageid) {
		try {

			Context httpCtx = Context.current();
			WebContext webCtx = ContextUtils.getWebContext(httpCtx);
			String ltc = webCtx.getLtc();
			// 如果获取不到Ltc则说明用户请求是非浏览器行为
			if (ltc == null || ltc.length() == 0) {
				return badRequest("no ltc");
			}
			// 币种
			String currency = this.foundation.getCurrency();
			webCtx.setCurrencyCode(currency);
			int site = this.foundation.getSiteID();
			int lang = this.foundation.getLanguage();
			String vhost = this.foundation.getVhost();
			String ip = this.foundation.getClientIP();
			List<CartItem> items = cartService
					.getAllItems(site, lang, currency);
			
			//保存选择的仓库id
			CookieUtils.setCookie("storageid", storageid+"", Context.current());
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
			
			// 判断所有商品是否是同一个仓库
			Integer firstStorage = items.get(0).getStorageID();
			if (firstStorage == null) {
				Logger.debug("storage id is null in cart");
				return badRequest("storage id is null in cart");
			}
			//~ 获取真实仓库
			int tstorid = firstStorage;
			List<dto.Storage> storagelist = iStorageService.getAllStorages();
			List<dto.Storage> newstoragelist = Lists.newArrayList(Collections2
					.filter(storagelist, c -> c.getIparentstorage() == tstorid));
			if (newstoragelist != null && newstoragelist.size() > 0) {
				firstStorage = newstoragelist.get(0).getIid();
				Logger.debug("get real storage -- > {} -- {} ",tstorid,firstStorage);
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
									List<String> clisting = Lists.transform(
											childs, c -> c.getClistingid());
									listingId.addAll(clisting);
								}
							});

			//~ 多仓库屏蔽统一仓库验证
/*			boolean isSameStorage = shippingServices.isSameStorage(listingId,
					firstStorage.toString());
			if (!isSameStorage) {
				Logger.debug("storage do not same when place order");
				return badRequest("storage do not same when place order");
			}*/

			Optional<String> source = FluentIterable.from(sourceProviders)
					.transform(sp -> sp.getSource(Context.current()))
					.filter(x -> x != null).first();
			String origin = source.orNull();

			List<LoyaltyPrefer> prefers = null;
			LoginContext loginCtx = this.foundation.getLoginContext();
			String email = null;
			if (loginCtx != null && loginCtx.isLogin()) {
				email = loginCtx.getMemberID();
				prefers = prefer.getAllPreferByEmail(email, items, webCtx);
			}

			CreateOrderRequest request = new CreateOrderRequest(items, site,
					null, null, origin, null, ip, lang, currency, vhost,
					prefers, firstStorage);

			Order order = this.orderService
					.createOrderInstanceForSubtotal(request);
			if (order.getIid() != null) {
				cartService.deleteItem(items);
				if (email != null) {
					// 将订单对象传入优惠信息中
					if (null != prefers && prefers.size() > 0) {
						for (int i = 0; i < prefers.size(); i++) {
							prefers.get(i).setOrder(order);
						}
					}
					prefer.saveAllPrefer(email, prefers, webCtx);
				}
			}

			String orderNum = order.getCordernumber();
			String returnUrl = controllers.paypal.routes.ExpressCheckoutNvp
					.confirmOrderView(null, null, orderNum).absoluteURL(
							Context.current().request());
			Logger.debug("paypal returnUrl:{}", returnUrl);

			String cancalUrl = controllers.cart.routes.Cart.cartview()
					.absoluteURL(Context.current().request());
			Logger.debug("paypal cancalUrl:{}", cancalUrl);

			SetExpressCheckout setEc = new SetExpressCheckout(orderNum,
					returnUrl, cancalUrl);
			setEc.setUsePaypalShipping(true);
			setEc.setEc(true);
			PaypalNvpPaymentStatus status = service.setExpressCheckout(setEc,
					webCtx);
			if (status != null && status.isNextStep()) {
				return redirect(status.getRedirectURL());

			}
			return redirect(controllers.order.routes.OrderProcessing
					.paymentFail(status.getOrderNum(), status.getFailedInfo(),
							status.getErrorCode()));
		} catch (Exception e) {
			Logger.debug("express checkout for cart  failed", e);
			return badRequest("express checkout for cart  failed");
		}
	}

	public Result confirmOrderView(String token, String PayerID, String n) {
		MetaUtils.currentMetaBuilder().setTitle("Payment");

		if(PayerID!=null && !"".equals(PayerID)){
			response().setCookie("payerid", PayerID);
		}else{
			Cookie source = request().cookie("payerid");
			if(source!=null && !"".equals(source.value()) && (PayerID==null || "".equals(PayerID))){
				PayerID = source.value();
			}
		}
		// 为什么要立刻保存一次信息而不是等用户确定后再来保存信息
		// 这样做事因为如果用户到了确认页面没有去支付,那么等用户再次去支付的时候不用再次填写信息
		service.saveShipAddress(token, PayerID, n);
		int language = foundation.getLanguage();
		PaymentContext paymentCtx = orderService.getPaymentContext(n, language);

		Order order = paymentCtx.getOrder().getOrder();
		String countryCode = order.getCcountrysn();
		Country country = countryService
				.getCountryByShortCountryName(countryCode);

		MemberAddress address = new MemberAddress();
		address.setCfirstname(order.getCfirstname());
		address.setCmiddlename(order.getCmiddlename());
		address.setClastname(order.getClastname());
		address.setCstreetaddress(order.getCstreetaddress());
		address.setCcity(order.getCcity());
		if (country != null) {
			address.setIcountry(country.getIid());
		}
		address.setCprovince(order.getCprovince());
		address.setCpostalcode(order.getCpostalcode());
		address.setCtelephone(order.getCtelephone());

		List<Country> countries = countryService.getAllCountries();
		Map<Integer, Country> countryMap = Maps.newLinkedHashMap();
		FluentIterable.from(countries).forEach(c -> {
			countryMap.put(c.getIid(), c);
		});

		String shipToCountry = order.getCcountry();
		// 判断用户邮寄的国家是否支持
		String shipToCountryCode = null;
		boolean isShipable = countryService.isShipable(country);
		if (isShipable) {
			shipToCountryCode = countryCode;
		}
		// String formActionUrl = controllers.paypal.routes.ExpressCheckoutNvp
		// .confirmOrder().absoluteURL(Context.current().request());
		String currencyCode = order.getCcurrency();
		Currency currency = currencyService.getCurrencyByCode(currencyCode);

		ExistingOrderContext orderCtx = new ExistingOrderContext(order,
				paymentCtx.getOrder().getDetails(), false);
		orderCtx.setCountry(country);
		orderCtx.setStorageId(order.getIstorageid());

		List<String> renderNames = Lists.newLinkedList();
		renderNames.add("cart-product");
		// renderNames.add("shipping-method");

		ExistingOrderComposite composite = productRender.getOrderComposite(
				orderCtx, renderNames);
		composite.setConfirmView(true);

		return ok(views.html.paypal.confirm_order_new.render(token, PayerID, n,
				address, shipToCountry, shipToCountryCode, composite,
				compositeRenderer, order, countries, false, currency));
	}

	/**
	 * 
	 * @return token
	 */
	public Result setExpressCheckout(String ordernum) {
		if (ordernum == null || ordernum.length() == 0) {
			throw new NullPointerException("ordernum is null");
		}

		Context httpCtx = Context.current();
		WebContext webCtx = ContextUtils.getWebContext(httpCtx);

		// 币种
		String currency = foundation.getCurrency();
		webCtx.setCurrencyCode(currency);

		String returnUrl = controllers.paypal.routes.ExpressCheckoutNvp
				.DoExpressCheckoutPayment(null, null, ordernum).absoluteURL(
						Context.current().request());
		Logger.debug("paypal returnUrl:{}", returnUrl);

		String cancalUrl = controllers.cart.routes.Cart.cartview().absoluteURL(
				Context.current().request());
		Logger.debug("paypal cancalUrl:{}", cancalUrl);

		SetExpressCheckout setEc = new SetExpressCheckout(ordernum, returnUrl,
				cancalUrl);

		PaypalNvpPaymentStatus status = service.setExpressCheckout(setEc,
				webCtx);

		if (status != null && status.isNextStep()) {
			Logger.debug("redirect:{}", status.getRedirectURL());
			return redirect(status.getRedirectURL());
		}
		//如果出现10486错误代码 ，重复token，就重新请求
		if(status.getErrorCode()!=null && ("10486".equals(status.getErrorCode()) || 
				"10409".equals(status.getErrorCode()))){
			return redirect("/paypal/ec?ordernum=" + status.getOrderNum());
		}
		return redirect(controllers.order.routes.OrderProcessing.paymentFail(
				status.getOrderNum(), status.getFailedInfo(),
				status.getErrorCode()));
	}

	public Result DoExpressCheckoutPayment(String token, String PayerID,
			String n) {
		boolean dropShip = false;
		if(n != null && n.endsWith("-DS")){
			dropShip = true;
		}
		
		if(n != null && !dropShip){
			Order order = this.orderMapper.getOrderByOrderNumber(n);
			// 如果用户使用优惠后导致付款金额为0或负值时,不能去付款
			Double grandtotal = order.getFgrandtotal();
			if (grandtotal == null || grandtotal <= 0) {
				return redirect(controllers.order.routes.OrderProcessing
						.paymentFailed("",
								"Order total is invalid.", "", ""));
			}
		}
		

		// 重试url
		String retryUrl = controllers.paypal.routes.ExpressCheckoutNvp
				.confirmOrderView(token, PayerID, n).absoluteURL(
						Context.current().request());

		PaypalNvpPaymentStatus status = service.DoExpressCheckoutPayment(token,
				PayerID, n);
		String orderNum = status.getOrderNum();
		if (status.isCompleted()) {
			Logger.debug("DoExpressCheckoutPayment successed");
			return redirect(controllers.order.routes.OrderProcessing
					.paymentConfirmed(orderNum));
		}
		LoginContext loginCtx = this.foundation.getLoginContext();
		if (loginCtx.isLogin()) {
			return redirect(controllers.order.routes.OrderProcessing
					.paymentFail(orderNum, status.getFailedInfo(),
							status.getErrorCode()));
		} else {
			return redirect(controllers.order.routes.OrderProcessing
					.paymentFailed(orderNum,
							status.getFailedInfo(), status.getErrorCode(),
							retryUrl));
		}

	}

	/**
	 * 处理用户确认过的订单
	 * 
	 * @return
	 */
	public Result confirmOrder() {
		Form<OrderConfirmForm> form = Form.form(OrderConfirmForm.class)
				.bindFromRequest();
		if (form.hasErrors()) {
			return badRequest("Incomplete parameter");
		}

		OrderConfirmForm ocf = form.get();
		String orderNum = ocf.getOrderNum();

		// 检查订单是否已经支付完成
		boolean isAlreadyPaid = orderService.isAlreadyPaid(orderNum);
		if (isAlreadyPaid) {
			return redirect(controllers.order.routes.OrderProcessing
					.paymentFail(orderNum,
							"Your order has been paid completed", ""));
		}
		// 判断国家是否是可发货的
		String countryCode = ocf.getCountryCode();
		Logger.debug("countryCode:{}", countryCode);
		Country country = countryService
				.getCountryByShortCountryName(countryCode);

		boolean isShipable = countryService.isShipable(country);
		Logger.debug("isShipable:{}", isShipable);
		Logger.debug("isShipable:{}", country.getBshow());
		if (!isShipable) {
			StringBuilder errorInfo = new StringBuilder();
			errorInfo.append(country.getCname());
			errorInfo.append(" shipping unavailable");
			return redirect(controllers.order.routes.OrderProcessing
					.paymentFail(orderNum, errorInfo.toString(), null));
		}

		int lang = this.foundation.getLanguage();
		Integer shipMethod = ocf.getShippingMethodIdValue();
		Order originalOrder = orderMapper.getOrderByOrderNumber(ocf
				.getOrderNum());

		String currency = originalOrder.getCcurrency();

		List<valueobjects.order_api.cart.CartItem> items = orderDetailDao
				.selectCartItemsByOrderNum(orderNum);

		List<String> listingId = Lists.newLinkedList();
		FluentIterable
				.from(items)
				.forEach(
						item -> {
							if (item instanceof valueobjects.order_api.cart.SingleCartItem) {
								listingId.add(item.getClistingid());
							} else if (item instanceof valueobjects.order_api.cart.BundleCartItem) {
								List<SingleCartItem> childs = ((valueobjects.order_api.cart.BundleCartItem) item)
										.getChildList();
								List<String> clisting = Lists.transform(childs,
										c -> c.getClistingid());
								listingId.addAll(clisting);
							} else {
								listingId.add(item.getClistingid());
							}
						});

		Double weight = freightService.getTotalWeight(items);

		// 开始核对邮费方式是否正确
		boolean correct = orderService.checkShippingMethodCorrect(
				originalOrder.getIstorageid(), countryCode, shipMethod, weight,
				originalOrder.getFordersubtotal(), listingId, currency, lang);
		if (!correct) {
			return redirect(controllers.order.routes.OrderProcessing
					.paymentFail(orderNum, "shipping method is not correct",
							null));
		}
		String token = ocf.getToken();
		String PayerID = ocf.getPayerID();

		Order order = new Order();
		order.setCordernumber(ocf.getOrderNum());
		order.setCfirstname(ocf.getFirstName());
		order.setClastname(ocf.getLastName());
		String street = ocf.getAddress1();
		if (ocf.getAddress2() != null) {
			street = street + " " + ocf.getAddress2();
		}
		order.setCstreetaddress(street);
		order.setCprovince(ocf.getProvince());
		order.setCcity(ocf.getCity());
		order.setCpostalcode(ocf.getZipCode());
		order.setCtelephone(ocf.getTelephone());
		order.setCmessage(ocf.getLeaveMessage());
		order.setCcountry(ocf.getCountry());
		order.setCcountrysn(ocf.getCountrysn());
		// 开始计算邮费
		order.setIshippingmethodid(shipMethod);
		Logger.debug("shipMethod:{}", shipMethod);
		String shipToCountryCode = ocf.getCountryCode();

		double shipPrice = orderService.getFreight(orderNum,
				shipMethod.toString(), shipToCountryCode);
		order.setFshippingprice(shipPrice);

		// 如果已经计算一次邮费了那么需要把以前的邮费减掉
		double total = originalOrder.getFgrandtotal();
		Double originalShipPrice = originalOrder.getFshippingprice();
		if (originalShipPrice != null && originalShipPrice > 0) {
			total = total - originalShipPrice;
		}

		total = total + shipPrice;

		// delete comma
		String money = Utils.money(total, currency);
		money = money.replaceAll(",", "");
		total = Double.parseDouble(money);
		order.setFgrandtotal(total);

		orderService.updateShipAddressAndShipPrice(order);
		return this.DoExpressCheckoutPayment(token, PayerID, orderNum);
	}

	public Result setExpressCheckoutForOrder(String orderNum) {
		if (orderNum == null || orderNum.length() == 0) {
			return redirect(controllers.order.routes.OrderProcessing
					.paymentFail("", "your order num is null", ""));
		}
		try {
			Context httpCtx = Context.current();
			WebContext webCtx = ContextUtils.getWebContext(httpCtx);

			String returnUrl = controllers.paypal.routes.ExpressCheckoutNvp
					.confirmOrderView(null, null, orderNum).absoluteURL(
							Context.current().request());
			Logger.debug("paypal returnUrl:{}", returnUrl);

			String cancalUrl = controllers.cart.routes.Cart.cartview()
					.absoluteURL(Context.current().request());
			Logger.debug("paypal cancalUrl:{}", cancalUrl);

			SetExpressCheckout setEc = new SetExpressCheckout(orderNum,
					returnUrl, cancalUrl);
			setEc.setUsePaypalShipping(true);
			setEc.setEc(true);
			PaypalNvpPaymentStatus status = service.setExpressCheckout(setEc,
					webCtx);
			if (status != null && status.isNextStep()) {
				return redirect(status.getRedirectURL());

			}
			return redirect(controllers.order.routes.OrderProcessing
					.paymentFail(status.getOrderNum(), status.getFailedInfo(),
							status.getErrorCode()));
		} catch (Exception e) {
			Logger.debug("express checkout for cart  failed", e);
			return redirect(controllers.order.routes.OrderProcessing
					.paymentFail(
							orderNum,
							"exception occurred when express checkout for order",
							""));
		}
	}

	public Result setExpressCheckoutForDropShipping(String dropShippingID) {
		LoginContext loginCtx = this.foundation.getLoginContext();
		if (!loginCtx.isLogin()) {
			return redirect(controllers.order.routes.OrderProcessing
					.paymentFail("", "you have to log in first", ""));
		}
		
		return this.setExpressCheckout(dropShippingID);
	}
}
