package controllers.mobile.about;

import interceptor.VisitLog;
import interceptor.auth.LoginAuth;
import interceptor.auth.TokenAuth;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import context.WebContext;
import play.Logger;
import play.data.DynamicForm;
import play.data.Form;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http.Context;
import play.mvc.Result;
import play.mvc.With;
import services.ICountryService;
import services.ICurrencyService;
import services.base.utils.MetaUtils;
import services.base.utils.Utils;
import services.loyalty.IPreferService;
import services.mobile.MobileService;
import services.mobile.member.LoginService;
import services.mobile.order.CartInfoService;
import services.order.IFreightService;
import services.order.IOrderService;
import services.order.IOrderStatusService;
import services.payment.IWebPaymentService;
import services.paypal.IExpressCheckoutNvpService;
import services.product.IProductLabelServices;
import services.search.criteria.ProductLabelType;
import services.shipping.IShippingMethodService;
import utils.CommonDefn;
import utils.MsgUtils;
import utils.ValidataUtils;
import valueobjects.base.LoginContext;
import valueobjects.loyalty.LoyaltyPrefer;
import valueobjects.order_api.CreateOrderRequest;
import valueobjects.order_api.OrderItem;
import valueobjects.order_api.ShippingMethodInformation;
import valueobjects.order_api.cart.CartItem;
import valueobjects.order_api.payment.PaymentContext;
import valueobjects.order_api.shipping.ShippingMethodRequst;
import valueobjects.paypal_api.PaypalNvpPaymentStatus;
import valueobjects.paypal_api.SetExpressCheckout;
import valuesobject.mobile.BaseResultType;
import dto.Country;
import dto.Currency;
import dto.ShippingMethodDetail;
import dto.member.MemberAddress;
import dto.order.Order;
import dto.payment.WebPaymentResult;

public class OrderPayController extends Controller {
	@Inject
	MobileService mobileService;
	@Inject
	IWebPaymentService paymentService;
	@Inject
	IOrderService orderService;
	@Inject
	LoginService loginService;
	@Inject
	IOrderStatusService statusService;
	@Inject
	ICurrencyService currencyService;

	@Inject
	IExpressCheckoutNvpService service;

	@Inject
	CartInfoService cartInfoService;
	@Inject
	IPreferService prefer;
	@Inject
	ICountryService countryService;
	@Inject
	private IFreightService freightService;
	@Inject
	private IShippingMethodService shippingMethodService;
	@Inject
	IProductLabelServices productLabelServices;
	/**
	 * 去支付页面
	 * 
	 * @param corderId
	 * 
	 * @return
	 */
	@With({ VisitLog.class, TokenAuth.class, LoginAuth.class })
	public Result payOrder(String corderId) {
		String email = loginService
				.getLoginMemberEmail(CommonDefn.ISLOGINTRUEEMAIL);
		Integer languageId = mobileService.getLanguageID();
		Map<String, Object> objMap = new HashMap<String, Object>();
		PaymentContext paymentContext = orderService.getPaymentContext(
				corderId, languageId);
		Order order = paymentContext.getOrder().getOrder();
		if (order == null) {
			objMap.put("re", BaseResultType.ORDER_PAY_NOT_FIND_ERROR_CODE);
			objMap.put("msg",
					MsgUtils.msg(BaseResultType.ORDER_PAY_NOT_FIND_ERROR_MSG));
			return ok(Json.toJson(objMap));
		} else {
			if (!ValidataUtils.validataStr(order.getCmemberemail()).equals(
					email)) {
				objMap.put("re",
						BaseResultType.ORDER_PAY_NOT_EQUALS_EMAIL_ERROR_CODE);
				objMap.put(
						"msg",
						MsgUtils.msg(BaseResultType.ORDER_PAY_NOT_EQUALS_EMAIL_ERROR_MSG));
				return ok(Json.toJson(objMap));
			}
			Integer ost = statusService
					.getIdByName(IOrderStatusService.PAYMENT_PENDING);
			Integer status = order.getIstatus();
			if (ValidataUtils.validataInt(ost) != status) {
				objMap.put(
						"re",
						BaseResultType.ORDER_PAY_STATUS_NOT_PAYMENT_PENDING_ERROR_CODE);
				objMap.put(
						"msg",
						MsgUtils.msg(BaseResultType.ORDER_PAY_STATUS_NOT_PAYMENT_PENDING_ERROR_MSG));
				return ok(Json.toJson(objMap));
			}
		}
		Currency currency = paymentContext.getCurrency();
		if (currency == null) {
			objMap.put("re", BaseResultType.ORDER_PAY_CURRENCY_ERROR_CODE);
			objMap.put("msg",
					MsgUtils.msg(BaseResultType.ORDER_PAY_CURRENCY_ERROR_MSG));
			return ok(Json.toJson(objMap));
		}
		if (order.getFshippingprice() == null) {
			WebContext webCtx = mobileService.getWebContext();
			String host = request().host();
			String cancalUrl = "http://" + host + "/mobile/api/cart/show";
			String returnUrl = controllers.mobile.about.routes.OrderPayController
					.orderViewConfirm(null, null, corderId).absoluteURL(
							Context.current().request());
			Logger.debug("paypal returnUrl:{}", returnUrl);
			Logger.debug("paypal cancalUrl:{}", cancalUrl);
			SetExpressCheckout setEc = new SetExpressCheckout(corderId, returnUrl,
					cancalUrl);
			setEc.setUsePaypalShipping(true);
			setEc.setEc(true);
			
			PaypalNvpPaymentStatus status = service.setExpressCheckout(setEc, webCtx);
			if (status != null && status.isNextStep()) {
				return redirect(status.getRedirectURL());
			}else{
				//错误时返回的页面
				return ok(views.html.mobile.pay.pay_failed.render(
						status.getFailedInfo(),status.getErrorCode(), null));
			}
		}
		//PaymentPaypalParam param = new PaymentPaypalParam(corderId, languageId);
		WebPaymentResult paypalForm = null;
		try {
			String pf = controllers.mobile.about.routes.OrderPayController
					.newPayOrder(corderId).absoluteURL(
							Context.current().request());
			paypalForm = new WebPaymentResult(pf);
		} catch (Exception e) {
			Logger.error("get paypal form error", e.fillInStackTrace());
			e.printStackTrace();
			objMap.put("re", BaseResultType.EXCEPTION);
			objMap.put("msg", MsgUtils.msg(BaseResultType.EXCEPTIONMSG));
			return ok(Json.toJson(objMap));
		}
		return ok(views.html.mobile.orderDetail.render(order, currency,
				paypalForm));
	}

	/**
	 * 支付完成return页
	 * 
	 * @param corderId
	 * 
	 * @return
	 */
	public Result finishPay(String oid, String code, String info) {
		return ok(views.html.mobile.finish_paypal.render(oid, code, info));
	}

	public Result newPayOrder(String oid) {
		Map<String, Object> objMap = new HashMap<String, Object>();
		if (StringUtils.isBlank(oid)) {
			objMap.put("re", BaseResultType.ORDER_PAY_NOT_FIND_ERROR_CODE);
			objMap.put("msg",
					MsgUtils.msg(BaseResultType.ORDER_PAY_NOT_FIND_ERROR_MSG));
			return ok(Json.toJson(objMap));
		}
		//Context httpCtx = Context.current();
		String returnUrl = controllers.mobile.about.routes.OrderPayController
				.DoOrderPayment(null, null, oid).absoluteURL(
						Context.current().request());
		String cannelUrl = controllers.mobile.about.routes.OrderPayController
				.finishPay(oid, null, null).absoluteURL(
						Context.current().request());
		SetExpressCheckout sec = new SetExpressCheckout(oid, returnUrl,
				cannelUrl);
		PaypalNvpPaymentStatus status = service.setExpressCheckout(sec,
				mobileService.getWebContext());
		if (status != null && status.isNextStep()) {
			return redirect(status.getRedirectURL());
		}
		String code = status.getFailedInfo();
		String info = status.getErrorCode();
		return ok(views.html.mobile.finish_paypal.render(oid, code, info));
	}

	public Result DoOrderPayment(String token, String palpayid, String n) {
		PaypalNvpPaymentStatus status = service.DoExpressCheckoutPayment(token,
				palpayid, n);
		if (status.isCompleted()) {
			Logger.debug("DoExpressCheckoutPayment successed");
			return ok(views.html.mobile.finish_paypal.render(n, "success",
					"Pay successed"));
		}
		String code = status.getFailedInfo();
		String info = status.getErrorCode();
		return ok(views.html.mobile.finish_paypal.render(n, code, info));
	}
	
	@With({ VisitLog.class, TokenAuth.class })
	public Result quickPayment(){
		Map<String, Object> objMap = new HashMap<String, Object>();
		try {
			
			WebContext webCtx = mobileService.getWebContext();
			//获取设置货币
			String currency = this.mobileService.getCurrency();
			webCtx.setCurrencyCode(currency);
			int site = this.mobileService.getWebSiteID();
			int lang = this.mobileService.getLanguageID();
			String vhost = this.mobileService.getMobileContext().getHost();
			String ip = this.mobileService.getMobileContext().getIp();
			String device = this.mobileService.getAppName();
			String uuid = this.mobileService.getUUID();
			//获取购物车
			facades.cart.Cart cart = cartInfoService.getCurrentCart(uuid, true);
			List<CartItem> items = cart.getAllItems();
			String host = request().host();
			String cancalUrl = "http://" + host + "/mobile/api/cart/show";
			if (items == null || items.size() == 0) {
				return redirect(cancalUrl);
			}
			
			List<valueobjects.cart.CartItem> newCart = cartInfoService.transformCart(items);
			List<LoyaltyPrefer> prefers = null;
			LoginContext loginCtx = this.loginService.getLoginContext();
			String email = null;
			if (loginCtx != null && loginCtx.isLogin()) {
				email = loginCtx.getMemberID();
				prefers = prefer.getAllPreferByEmail(email, newCart, webCtx);
			}
			CreateOrderRequest request = new CreateOrderRequest(newCart, site,
					null, null, device, null, ip, lang, currency, vhost,
					prefers, null);

			Order order = this.orderService
					.createOrderInstanceForSubtotal(request);
			//生成订单
			String ordernum = order.getCordernumber();
			String cid = cart.getId();
			if (ordernum != null) {
				if(cid != null){
					cartInfoService.deleteCart(cid);
				}
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
			
			String returnUrl = controllers.mobile.about.routes.OrderPayController
					.orderViewConfirm(null, null, ordernum).absoluteURL(
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
			}else{
			//错误时返回的页面
			return ok(views.html.mobile.pay.pay_failed.render(
					status.getFailedInfo(),status.getErrorCode(), null));
			}
		}catch (Exception e) {
			Logger.error("express checkout for cart  failed", e.fillInStackTrace());
			e.fillInStackTrace();
			objMap.put("re", BaseResultType.EXCEPTION);
			objMap.put("msg", MsgUtils.msg(BaseResultType.EXCEPTIONMSG));
			return ok(Json.toJson(objMap));
		}
	}
	
	public Result orderViewConfirm(String token, String PayerID, String n) {
		MetaUtils.currentMetaBuilder().setTitle("Payment");
Logger.info("-----------------1");
		// 为什么要立刻保存一次信息而不是等用户确定后再来保存信息
		// 这样做事因为如果用户到了确认页面没有去支付,那么等用户再次去支付的时候不用再次填写信息
		service.saveShipAddress(token, PayerID, n);
		int language = mobileService.getLanguageID();
		PaymentContext paymentCtx = orderService.getPaymentContext(n, language);

		Order order = paymentCtx.getOrder().getOrder();
		Logger.info("-----------------order="+JSONObject.toJSONString(order));
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
	
		return ok(views.html.mobile.pay.paypal_order_confirm.render(token,PayerID,n,address, 
				shipToCountry, shipToCountryCode,countries,order,currency,orderList));
	}
	
	public Result orderQuickPayConfirm(){
		Logger.debug("=========================orderQuickPayConfirm=========================");
		DynamicForm df = Form.form().bindFromRequest();
		String orderNum = df.get("orderNum");
		Logger.debug("========================={}=========================",orderNum);
		// 检查订单是否已经支付完成
		boolean isAlreadyPaid = orderService.isAlreadyPaid(orderNum);
		if (isAlreadyPaid) {
			return ok(views.html.mobile.pay.pay_failed.render(
					"Your order has been paid completed", orderNum,null));
		}
		
		// 判断国家是否是可发货的
		String countryCode = df.get("countrysn");
		Logger.debug("countryCode:{}", countryCode);
		Country country = countryService
				.getCountryByShortCountryName(countryCode);

		boolean isShipable = countryService.isShipable(country);
		Logger.debug("isShipable:{}", isShipable);
		if (!isShipable) {
			StringBuilder errorInfo = new StringBuilder();
			errorInfo.append(country.getCname());
			errorInfo.append(" shipping unavailable");
			return ok(views.html.mobile.pay.pay_failed.render(
					errorInfo.toString(),orderNum,null));
		}
		
		Integer lang = mobileService.getLanguageID();
		String token = df.get("ptoken");
		String PayerID = df.get("PayerID");
		Logger.info("---------token="+token+",PayerID="+PayerID);
		// 重试url
		String retryUrl = controllers.mobile.about.routes.OrderPayController
				.orderViewConfirm(token, PayerID, orderNum).absoluteURL(
				Context.current().request());
				
		String shipMethod = df.get("shippingMethodIdValue");
		Logger.debug("shipMethod========================={}=========================",shipMethod);
		if(null == shipMethod || "".equals(shipMethod)){
	
			return ok(views.html.mobile.pay.pay_failed.render(
					"shipping method can not find",orderNum,retryUrl));
		}
		Integer ship = Integer.parseInt(shipMethod);
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
		/*已经切换新的物流方式，不需要检查物流方式了**/
/*		if(items.size() > 0){
			Double weight = freightService.getTotalWeight(items);
	
			// 开始核对邮费方式是否正确
			boolean correct = orderService.checkShippingMethodCorrect(
					originalOrder.getIstorageid(), countryCode, ship, weight,
					fordersubtotal, listingId, currency, lang);
			
			if (!correct) {
				return ok(views.html.mobile.pay.pay_failed.render(
						"shipping method is not correct",orderNum,null));
			}
		}else{
			return ok(views.html.mobile.pay.pay_failed.render(
					"item not found",orderNum,null));
		}*/
		
		String firstName = df.get("firstName");
		String lastName = df.get("lastName");
		String address1 = df.get("address1");
		String province = df.get("province");
		String city = df.get("city");
		String postalcode = df.get("zipCode");
		String telephone = df.get("telephone");
		String message = df.get("leaveMessage");
		String countryFull = df.get("country");
		Logger.info("-----orderQuickPayConfirm= ship="+ship+",zipCode="+postalcode+",firstName="+firstName+",lastName="+lastName
				+",address1="+address1+",province="+province+",city="+city+",telephone="+telephone+",leaveMessage="+message+
				",country="+country+",orderNum="+orderNum+",countrysn="+countryCode+",shippingMethodIdValue="+shipMethod+
				",ptoken="+token+",PayerID="+PayerID);
		Order order = new Order();
		order.setCordernumber(orderNum);
		order.setCfirstname(firstName);
		order.setClastname(lastName);
		String street = address1;
		order.setCstreetaddress(street);
		order.setCprovince(province);
		order.setCcity(city);
		order.setCpostalcode(postalcode);
		order.setCtelephone(telephone);
		order.setCmessage(message);
		order.setCcountry(countryFull);
		order.setCcountrysn(countryCode);
		// 开始计算邮费
		order.setIshippingmethodid(ship);
		String code = MsgUtils.get(ship+"");
		order.setCshippingcode(code);//code
		Logger.debug("shipMethod:{}", shipMethod);
		
/*		Double weight = freightService.getTotalWeight(items);
		Double shippingWeight = freightService.getTotalShipWeight(items);

		ShippingMethodDetail shippingMethod = shippingMethodService
				.getShippingMethodDetail(ship, lang);
		int site = this.mobileService.getWebSiteID();
		Logger.debug("*************************");
		Logger.debug("weight:{}", weight);
		Logger.debug("shippingWeight:{}", shippingWeight);
		Logger.debug("shippingMethod:{}", shippingMethod.getIid());
		Logger.debug("Country:{}", country.getCname());
		Logger.debug("baseTotal:{}", fordersubtotal);
		Logger.debug("currencyCode:{}", currency);
		Logger.debug("site:{}", site);
		Logger.debug("listingId:{}", listingId.toString());
		Logger.debug("*************************");
		boolean hasAllFreeShipping = hasAllFreeShipping(listingId);
		Double freight = freightService.getFinalFreight(shippingMethod, weight,
				shippingWeight, currency, fordersubtotal, hasAllFreeShipping);
		if (null == freight) {
			return null;
		}//getBistracking
		String shippingContext = shippingMethod.getCcontent();
		shippingMethod.setBisspecial(false);
		shippingMethod.setBexistfree(false);
		shippingMethod.setBistracking(false);
		ShippingMethodInformation smi = new ShippingMethodInformation(
				shippingMethod, shippingContext, freight);
		ShippingMethodRequst requst = new ShippingMethodRequst(
				shippingMethod.getIstorageid(), country.getCshortname(),
				weight, shippingWeight, null, fordersubtotal, listingId,
				shippingMethod.getBisspecial(), currency, site,
				hasAllFreeShipping);
		List<ShippingMethodInformation> smiList = Lists.newArrayList(smi);
		smiList = shippingMethodService.processingInPlugin(smiList, requst);
		Double shipPrice = smiList.isEmpty() ? null : smiList.get(0).getFreight();
		if(shipPrice == null){
			shipPrice = 0.00;
		}*/
		String shipPriceSt = MsgUtils.get(ship+code);//运费
		Double shipPrice = new Double(shipPriceSt);
		if(shipPrice == null){
			shipPrice = 0.00;
		}
		order.setFshippingprice(Double.valueOf(shipPrice));

		// 如果已经计算一次邮费了那么需要把以前的邮费减掉
		Double total = originalOrder.getFgrandtotal();
		Double originalShipPrice = originalOrder.getFshippingprice();
		if (originalShipPrice != null && originalShipPrice > 0) {
			total = total - originalShipPrice;
		}

		total = total + Double.valueOf(shipPrice);

		// delete comma
		String money = Utils.money(total, currency);
		money = money.replaceAll(",", "");
		total = Double.parseDouble(money);
		order.setFgrandtotal(total);
		Logger.info("---order="+JSONObject.toJSONString(order));
		orderService.updateShipAddressAndShipPrice(order);
		
		// 如果用户使用优惠后导致付款金额为0或负值时,不能去付款
		Double grandtotal = order.getFgrandtotal();
		if (grandtotal == null || grandtotal <= 0) {
			return ok(views.html.mobile.pay.pay_failed.render(
					"Order total is invalid.", orderNum,null));
		}
		
		Logger.debug("paypal retryUrl:{}", retryUrl);
		PaypalNvpPaymentStatus status = service.DoExpressCheckoutPayment(token,
				PayerID, orderNum);
		if (status.isCompleted()) {
			Logger.debug("DoExpressCheckoutPayment successed");
			return ok(views.html.mobile.pay.pay_success.render(
					status.getOrderNum()));
		}
		if (loginService.isLogin()) {
			return ok(views.html.mobile.pay.pay_failed.render(
					status.getFailedInfo(),status.getErrorCode(),null));
		} else {
			return ok(views.html.mobile.pay.pay_failed.render(
					status.getFailedInfo(),status.getErrorCode(),retryUrl));
		}
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
