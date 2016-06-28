package controllers.mobile.paypal;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.elasticsearch.common.collect.Lists;

import play.Logger;
import play.data.Form;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http.Context;
import play.mvc.Result;
import plugins.mobile.order.OrderDetailPluginHelper;
import services.ICountryService;
import services.ICurrencyService;
import services.base.FoundationService;
import services.base.utils.MetaUtils;
import services.base.utils.Utils;
import services.cart.ICartServices;
import services.loyalty.IPreferService;
import services.mobile.order.OrderMobileService;
import services.order.IFreightService;
import services.order.IOrderService;
import services.paypal.IExpressCheckoutNvpService;
import services.product.IProductLabelServices;
import services.search.criteria.ProductLabelType;
import services.shipping.IShippingMethodService;
import services.shipping.IShippingServices;
import valueobjects.base.LoginContext;
import valueobjects.cart.CartItem;
import valueobjects.loyalty.LoyaltyPrefer;
import valueobjects.order.OrderConfirmFormV2;
import valueobjects.order_api.CreateOrderRequest;
import valueobjects.order_api.OrderItem;
import valueobjects.order_api.payment.PaymentContext;
import valueobjects.paypal_api.PaypalNvpPaymentStatus;
import valueobjects.paypal_api.SetExpressCheckout;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Maps;
import com.google.inject.Inject;

import context.ContextUtils;
import context.WebContext;
import dto.Country;
import dto.Currency;
import dto.member.MemberAddress;
import dto.order.Order;
import dto.order.OrderDetail;

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
	IShippingServices shippingServices;
	
	@Inject
	ICountryService countryService;
	
	@Inject
	ICurrencyService currencyService;
	
	@Inject
	OrderDetailPluginHelper pluginHelper;
	
	@Inject
	private IFreightService freightService;
	@Inject
	private IShippingMethodService shippingMethodService;
	@Inject
	IProductLabelServices productLabelServices;
	@Inject
	IPreferService prefer;
	
	@Inject
	ICartServices icartServices;
	
	@Inject
	OrderMobileService orderMobileService;
	

	/**
	 * 用户未登陆的情况下快捷支付
	 * 
	 */
	public Result setExpressCheckoutForCart() {
		try {
			Context httpCtx = Context.current();
			WebContext webCtx = ContextUtils.getWebContext(httpCtx);
			
			//获取设置货币
			String currency = this.foundation.getCurrency();
			webCtx.setCurrencyCode(currency);
			int site = this.foundation.getSiteID();
			int lang = this.foundation.getLanguage();
			String vhost = this.foundation.getVhost();
			String ip = this.foundation.getClientIP();
			String device = this.foundation.getDevice();
			//获取购物车
			List<CartItem> items = icartServices.getAllItemsCurrentStorageid(site, lang, currency);
			
			String host = request().host();
			String cancalUrl = "http://" + host + "/cart";
			if (items == null || items.size() == 0) {
				return redirect(cancalUrl);
			}
			
			List<LoyaltyPrefer> prefers = null;
			LoginContext loginCtx = this.foundation.getLoginContext();
			String email = null;
			if (loginCtx != null && loginCtx.isLogin()) {
				email = loginCtx.getMemberID();
				prefers = prefer.getAllPreferByEmail(email, items, webCtx);
			}
			CreateOrderRequest request = new CreateOrderRequest(items, site,
					null, null, device, null, ip, lang, currency, vhost,
					prefers, null);
			request.setCpaymenttype("paypal-ec");

			Order order = this.orderService
					.createOrderInstanceForSubtotal(request);
			//生成订单
			String ordernum = order.getCordernumber();
			if (ordernum != null) {
				icartServices.deleteItem(items);
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
			
			String returnUrl = controllers.mobile.paypal.routes.ExpressCheckoutNvp
					.confirmOrderView(null, null, ordernum).absoluteURL(
							Context.current().request());
			Logger.debug("paypal returnUrl:{}", returnUrl);
			Logger.debug("paypal cancalUrl:{}", cancalUrl);
			
			SetExpressCheckout setEc = new SetExpressCheckout(ordernum, returnUrl,
					cancalUrl);
			setEc.setUsePaypalShipping(true);
			setEc.setEc(true);
			
			PaypalNvpPaymentStatus status = service.setExpressCheckout(setEc, webCtx);
			if (status != null && status.isNextStep()) {
				return redirect(status.getRedirectURL());
			}
			//错误时返回的页面
			return ok(views.html.mobile.paypal.failed.render(status.getFailedInfo(),status.getErrorCode(),null));
		} catch (Exception e) {
			Logger.debug("express checkout for cart  failed", e);
			return badRequest("express checkout for cart  failed");
		}
	}

	public Result confirmOrderView(String token, String PayerID, String n) {
		MetaUtils.currentMetaBuilder().setTitle("Payment");

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
		if(order.getCstreetaddress() == null){
			address.setCstreetaddress("");
		}else{
			address.setCstreetaddress(order.getCstreetaddress());
		}
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
		String currencyCode = order.getCcurrency();
		Currency currency = currencyService.getCurrencyByCode(currencyCode);
		List<OrderItem> orderList = orderService.getOrderDetailByOrder(order,language);
	
		return ok(views.html.mobile.paypal.paypal_order_confirm.render(token,PayerID,n,address, 
				shipToCountry, shipToCountryCode,countries,order,currency,orderList));
	}
	
	public Result confirmOrder(){
		Form<OrderConfirmFormV2> form = Form.form(OrderConfirmFormV2.class)
				.bindFromRequest();
		if (form.hasErrors()) {
			return badRequest(form.errorsAsJson());
		}

		OrderConfirmFormV2 ocf = form.get();
		String orderNum = ocf.getOrderNum();

		// 检查订单是否已经支付完成
		boolean isAlreadyPaid = orderService.isAlreadyPaid(orderNum);
		if (isAlreadyPaid) {
			return ok(views.html.mobile.paypal.failed.render(
					"Your order has been paid completed", orderNum,null));
		}
		// 判断国家是否是可发货的
		String countryCode = ocf.getCountrysn();
		Logger.debug("countryCode:{}", countryCode);
		Country country = countryService
				.getCountryByShortCountryName(countryCode);

		boolean isShipable = countryService.isShipable(country);
		Logger.debug("isShipable:{}", isShipable);
		if (!isShipable) {
			StringBuilder errorInfo = new StringBuilder();
			errorInfo.append(country.getCname());
			errorInfo.append(" shipping unavailable");
			return ok(views.html.mobile.paypal.failed.render(
					errorInfo.toString(),orderNum,null));
		}
		int lang = this.foundation.getLanguage();
		Integer shipMethod = ocf.getShippingMethodIdValue();
		PaymentContext paymentCtx = orderService.getPaymentContext(orderNum, lang);
		Order originalOrder = paymentCtx.getOrder().getOrder();
		Double fordersubtotal = originalOrder.getFordersubtotal();
		String currency = originalOrder.getCcurrency();
		
		List<OrderItem> orderList = orderService.getOrderDetailByOrder(originalOrder,lang);
		
		List<String> listingId = Lists.newLinkedList();
		List<CartItem> items = new ArrayList<CartItem>();
		for (OrderItem orderItem : orderList) {
			CartItem ci = new CartItem();
			Logger.debug("listing ====" + orderItem.getClistingid());
			ci.setClistingid(orderItem.getClistingid());
			ci.setIqty(orderItem.getIqty());
			items.add(ci);
			listingId.add(orderItem.getClistingid());
		}
		if(items.size() ==0){
			return ok(views.html.mobile.paypal.failed.render(
					"item not found",orderNum,null));
		}
		
		String shipCode = ocf.getShipMethodCode();
		Integer storageid = originalOrder.getIstorageid();
		if(storageid==null){
			storageid = 1;
		}
		// 开始核对邮费方式是否正确
		valueobjects.order.ShippingMethod hit = orderMobileService.checkShippingMethodCorrect(
				storageid, countryCode, shipCode,
				originalOrder.getFordersubtotal(), items, currency, lang);
		if (hit == null) {
			return ok(views.html.mobile.paypal.failed.render(
					"shipping method is not correct",orderNum,null));
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
		order.setIshippingmethodid(ocf.getShipMethodId());
		order.setCshippingcode(ocf.getShipMethodCode());
		double shipPrice = hit.getPrice();
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
		
		// 如果用户使用优惠后导致付款金额为0或负值时,不能去付款
		Double grandtotal = order.getFgrandtotal();
		if (grandtotal == null || grandtotal <= 0) {
			return ok(views.html.mobile.paypal.failed.render(
					"Order total is invalid.", orderNum,null));
		}
		// 重试url
		String retryUrl = controllers.mobile.paypal.routes.ExpressCheckoutNvp
				.confirmOrderView(token, PayerID, orderNum).absoluteURL(
						Context.current().request());

		PaypalNvpPaymentStatus status = service.DoExpressCheckoutPayment(token,
				PayerID, orderNum);
		if (status.isCompleted()) {
			Logger.debug("DoExpressCheckoutPayment successed");
			return ok(views.html.mobile.order.pay.pay_success.render(
					status.getOrderNum(),null,null));
		}
		LoginContext loginCtx = this.foundation.getLoginContext();
		if (loginCtx.isLogin()) {
			return ok(views.html.mobile.paypal.failed.render(
					status.getFailedInfo(),status.getErrorCode(),null));
		} else {
			return ok(views.html.mobile.paypal.failed.render(
					status.getFailedInfo(),status.getErrorCode(),retryUrl));
		}
	}
	
	/**
	 * 
	 * @return token
	 */
	public Result setExpressCheckout(String ordernum) {
		if(ordernum == null || ordernum.length() == 0){
			throw new NullPointerException("ordernum is null");
		}
		
		Context httpCtx = Context.current();
		WebContext webCtx = ContextUtils.getWebContext(httpCtx);
		
		
		String returnUrl = controllers.mobile.paypal.routes.ExpressCheckoutNvp
				.DoExpressCheckoutPayment(null, null,ordernum).absoluteURL(
						Context.current().request());
		Logger.debug("paypal returnUrl:{}", returnUrl);
		String host = request().host();
		String cancalUrl = "http://" + host + "/cart";
		Logger.debug("paypal cancalUrl:{}", cancalUrl);
		
		SetExpressCheckout setEc = new SetExpressCheckout(ordernum, returnUrl,
				cancalUrl);
		

		PaypalNvpPaymentStatus status = service.setExpressCheckout(setEc,
				webCtx);
		
		if (status != null && status.isNextStep()) {
			return redirect(status.getRedirectURL());
		}
		String errorInfo = status.getFailedInfo();
		String errorCode = status.getErrorCode();
		return ok(views.html.mobile.order.pay.pay_failed.render(errorInfo,errorCode));
	}

	
	public Result DoExpressCheckoutPayment(String token, String PayerID,String n) {
		PaypalNvpPaymentStatus status = service.DoExpressCheckoutPayment(token,
				PayerID,n);
		if (status.isCompleted()) {
			Logger.debug("DoExpressCheckoutPayment successed");
			return redirect(controllers.mobile.paypal.routes.ExpressCheckoutNvp.successView(n));
		}
		String errorInfo = status.getFailedInfo();
		String errorCode = status.getErrorCode();
		return ok(views.html.mobile.order.pay.pay_failed.render(errorInfo,errorCode));
	}
	
	public Result successView(String orderNum){
		int language = foundation.getLanguage();
		PaymentContext paymentCtx = orderService.getPaymentContext(orderNum, language);
		
		String orderStr = null;
		String orderDetailStr = null;
		if(paymentCtx != null){
			Order order = paymentCtx.getOrder().getOrder();
			if(order != null){
//				ObjectMapper objectMapper = new ObjectMapper();
//				JSONObject json = objectMapper.convertValue(this, JSONObject.class);
				JsonNode json = Json.toJson(order);
				orderStr = json.toString();
			}
			List<OrderDetail> items = paymentCtx.getOrder().getDetails();
			if(items != null && items.size() > 0){
				JsonNode json = Json.toJson(items);
				orderDetailStr = json.toString();
			}
		}
		
		
		return ok(views.html.mobile.order.pay.pay_success.render(orderNum,orderStr,orderDetailStr));
	}
	
	/**
	 * 所有免邮
	 * 
	 * @return
	 */
	private boolean hasAllFreeShipping(List<String> listingids) {
		// ~ 所有免邮
		List<String> allfp = productLabelServices.getListByListingIdsAndType(
				listingids, ProductLabelType.AllFreeShipping.toString());
		return (allfp != null && allfp.size() > 0);
	}
	
}
