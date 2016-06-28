package services.paypal;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.inject.Inject;

import mapper.base.ProvinceMapper;
import mapper.order.OrderMapper;

import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;
import org.json.simple.JSONObject;

import play.Configuration;
import play.Logger;
import play.Play;
import play.libs.ws.WS;
import play.mvc.Http.Context;
import services.IWebsiteService;
import services.base.CountryService;
import services.base.FoundationService;
import services.base.utils.DoubleCalculateUtils;
import services.base.utils.Utils;
import services.order.IOrderEnquiryService;
import services.order.IOrderService;
import services.order.IOrderStatusService;
import services.order.IOrderUpdateService;
import services.order.dropShipping.DropShippingMapEnquiryService;
import services.order.dropShipping.DropShippoingToOrderService;
import services.payment.PaymentConfirmationService;
import valueobjects.base.LoginContext;
import valueobjects.order_api.ConfirmedOrder;
import valueobjects.order_api.OrderValue;
import valueobjects.order_api.payment.PaymentContext;
import valueobjects.paypal_api.PaypalNvpPaymentStatus;
import valueobjects.paypal_api.SetExpressCheckout;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.client.util.Maps;
import com.google.common.collect.FluentIterable;
import com.google.common.eventbus.EventBus;
import com.google.inject.Singleton;

import context.WebContext;
import dto.Country;
import dto.Province;
import dto.order.Order;
import dto.order.OrderDetail;
import entity.payment.PaymentBase;
import entity.payment.PaypaiReturnLog;
import events.order.PaymentConfirmationEvent;
import events.paypal.PaymentEvent;

/**
 * 
 * @author lijun
 *
 */
@Singleton
public class ExpressCheckoutNvpService implements IExpressCheckoutNvpService {
	// 超时时间
	public static final int TIMEOUT = 30 * 1000;
	public static final String TT_ACCOUNT = "ttAccount";
	private static final String TT_ORDER_ID = "ttOrderId";
	private static final String DROP_SHIP = "dropShip";

	// private String user;
	// private String pwd;
	// private String signature;
	private String version;
	// 是否沙箱
	private boolean sandbox = false;
	private String endPoint;
	private String redirectURL;
	private String img;
	private Configuration config;

	@Inject
	IOrderService orderService;

	@Inject
	FoundationService foundation;

	@Inject
	PaymentServices paymentservices;

	@Inject
	EventBus eventBus;

	@Inject
	IOrderUpdateService updateService;

	@Inject
	OrderMapper orderMapper;

	@Inject
	IOrderEnquiryService enquiryService;

	@Inject
	IWebsiteService websiteService;

	@Inject
	DropShippoingToOrderService dropShippoingToOrder;

	@Inject
	DropShippingMapEnquiryService dropShippingMapEnquiry;

	@Inject
	PaymentConfirmationService paymentEnquiry;
	
	@Inject
	CountryService countryService;
	
	@Inject
	ProvinceMapper provinceMapper;
	

	public ExpressCheckoutNvpService() {
		try {

			this.config = Play.application().configuration()
					.getConfig("paypal.payment");
			// this.user = config.getString("user");
			// this.pwd = config.getString("pwd");
			// this.signature = config.getString("signature");
			this.version = config.getString("version");
			if (version == null) {
				version = "98.0";
			}
			this.img = this.config.getString("img");

			Boolean isSandbox = Play.application().configuration()
					.getBoolean("paypal.sandbox");
			if (isSandbox != null) {
				this.sandbox = isSandbox;
			}
			Logger.debug("paypal expressCheckout sandbox:{}", this.sandbox);
			if (this.sandbox) {
				this.endPoint = config.getString("sandbox.endPoint.nvp");
				Logger.debug("paypal endPoint:{}", this.endPoint);
			} else {
				this.endPoint = config.getString("endPoint.nvp");
			}
			Logger.debug("paypal endPoint:{}", this.endPoint);
			if (this.sandbox) {
				this.redirectURL = config.getString("sandbox.redirectURL.nvp");
			} else {
				this.redirectURL = config.getString("redirectURL.nvp");
			}
			Logger.debug("paypal redirectURL:{}", this.redirectURL);
		} catch (Exception e) {
			Logger.debug("init ExpressCheckoutNvpService error", e);
		}
	}

	/**
	 * 确认配置参数能取到
	 */
	private void assertConfig() {
		if (this.endPoint == null || this.endPoint.length() == 0) {
			throw new NullPointerException(
					"can not find config:paypal.endPoint");
		}
		if (this.redirectURL == null || this.redirectURL.length() == 0) {
			throw new NullPointerException(
					"can not find config:paypal.redirectURL");
		}
	}

	/**
	 * 设置账户密码
	 * 
	 * @param paras
	 */
	private void setSecurity(Map<String, String> params, Order order) {
		String user = null;
		String pwd = null;
		String signature = null;
		// 不同金额用不同的账号
		PaymentBase account = null;
		String accountStr = null;
		if (!this.sandbox) {
			account = paymentservices.GetPayment(order);
			if (account == null) {
				throw new NullPointerException(
						"can not get paypal account from t_payment_paypal_account");
			}
			accountStr = account.getCbusiness();
			// 这个参数是自定义的
			params.put(TT_ACCOUNT, accountStr);

			user = account.getCuser();
			pwd = account.getCpwd();
			signature = account.getCsignature();

		} else {
			user = this.config.getString("sandbox.user");
			pwd = this.config.getString("sandbox.pwd");
			signature = this.config.getString("sandbox.signature");
		}
		if (user == null || user.length() == 0) {
			throw new NullPointerException("can not find config : user");
		}
		if (pwd == null || pwd.length() == 0) {
			throw new NullPointerException("can not find config : pwd");
		}
		if (signature == null || signature.length() == 0) {
			throw new NullPointerException("can not find config : signature");
		}

		params.put("USER", user);
		params.put("PWD", pwd);
		params.put("SIGNATURE", signature);
		params.put("VERSION", this.version);

	}

	/**
	 * 创建paypal express checkout 需传的参数
	 * 
	 * @param orderNum
	 * @return
	 */
	private Map<String, String> createTransactionDetails(String orderNum) {
		this.assertConfig();
		int langId = foundation.getLanguage();

		Map<String, String> params = Maps.newHashMap();

		PaymentContext context = null;
		if (orderNum != null && orderNum.endsWith("-DS")) {
			LoginContext loginCtx = this.foundation.getLoginContext();
			String email = loginCtx.getMemberID();
			context = dropShippoingToOrder.getPaymentContext(orderNum, email);
			params.put(DROP_SHIP, "1");

		} else {
			context = orderService.getPaymentContext(orderNum, langId);
		}

		if (context == null || context.getOrder()==null) {
			throw new NullPointerException("order number:" + orderNum
					+ " is invalid");
		}

		ConfirmedOrder order = context.getOrder();
		
		String paymentid = order.getOrder().getCpaymentid();
		Logger.debug("paymentid===={}",paymentid);

		// PaypalPayment payment = new PaypalPayment();
		// payment.setReturnUrl(context.getBackUrl());
		// payment.setOrder(context.getOrder().getOrder());
		// payment.setOrderDetails(context.getOrder().getDetails());
		// payment.setPaymentbase(paymentservices.GetPayment(context.getOrder()
		// .getOrder()));

		params.put(TT_ORDER_ID, order.getOrder().getIid() != null ? order
				.getOrder().getIid().toString() : order.getOrder()
				.getCordernumber());

		// 不同金额用不同的账号
		this.setSecurity(params, order.getOrder());

		// 公司image
		if (this.img != null) {
			params.put("HDRIMG", this.img);
		}

		// 这个参数不能改动
		params.put("PAYMENTREQUEST_0_PAYMENTACTION", "Sale");
		String currency = order.getOrder().getCcurrency();
		if (currency == null || currency.length() == 0) {
			Logger.debug("orderNum:{} currency is null", orderNum);
			throw new NullPointerException("currency is null");
		}
		// 币种
		params.put("PAYMENTREQUEST_0_CURRENCYCODE", currency);
		// 该订单最终用户需要支付总金额
		String total = Utils.money(order.getOrder().getFgrandtotal(), currency)
				.replaceAll(",", "");
		params.put("PAYMENTREQUEST_0_AMT", total);

		double itemsTotal = order.getOrder().getFordersubtotal();
		List<OrderDetail> items = order.getDetails();
		for (int i = 0; i < items.size(); i++) {
			OrderDetail od = items.get(i);
			String item_name = od.getCtitle();
			if (item_name == null || item_name.length() == 0) {
				item_name = od.getCsku();
			}
			params.put("L_PAYMENTREQUEST_0_AMT" + i,
					Utils.money(od.getFprice(), currency).replaceAll(",", ""));
			params.put("L_PAYMENTREQUEST_0_NUMBER" + i, od.getCsku());
			params.put("L_PAYMENTREQUEST_0_NAME" + i, item_name);
			params.put("L_PAYMENTREQUEST_0_QTY" + i, od.getIqty().toString());

		}

		// 邮费
		// If you specify a value for PAYMENTREQUEST_n_SHIPPINGAMT
		// you must also specify a value for PAYMENTREQUEST_n_ITEMAMT
		if (order.getOrder() != null
				&& order.getOrder().getFshippingprice() != null
				&& order.getOrder().getFshippingprice() != 0) {
			params.put("PAYMENTREQUEST_0_SHIPPINGAMT",
					Utils.money(order.getOrder().getFshippingprice(), currency)
							.replaceAll(",", ""));
		}
		// 折扣
		if (order.getOrder().getFextra() != null
				&& order.getOrder().getFextra() < 0) {
			double extra = new DoubleCalculateUtils(order.getOrder()
					.getFextra()).doubleValue();
			params.put("L_PAYMENTREQUEST_0_AMT" + items.size(),
					Utils.money(extra).replaceAll(",", ""));

			itemsTotal = itemsTotal + extra;
		}

		String itemsTotalStr = Utils.money(itemsTotal, currency).replaceAll(
				",", "");
		params.put("PAYMENTREQUEST_0_ITEMAMT", itemsTotalStr);

		StringBuilder userName = new StringBuilder();

		String firstName = order.getOrder().getCfirstname();
		if (firstName != null && firstName.length() > 0) {
			userName.append(firstName);
		}

		String middleName = order.getOrder().getCmiddlename();
		if (middleName != null && middleName.length() > 0) {
			userName.append(" ");
			userName.append(middleName);
		}

		String lastName = order.getOrder().getClastname();
		if (lastName != null && lastName.length() > 0) {
			userName.append(" ");
			userName.append(lastName);
		}
		// 如果订单中ccountrysn为空则代表是用户未登陆情况下快捷支付
		String country = order.getOrder().getCcountrysn();
		if (country != null && country.length() > 0) {
			// 设置shipping address
			params.put("PAYMENTREQUEST_0_SHIPTONAME", userName.toString());
			// 街道
			params.put("PAYMENTREQUEST_0_SHIPTOSTREET", order.getOrder()
					.getCstreetaddress());
			// city
			params.put("PAYMENTREQUEST_0_SHIPTOCITY", order.getOrder()
					.getCcity());
			
			//如果是paypal就传简拼
			if(paymentid!=null && "paypal".equals(paymentid)){
				Country coun = countryService.getCountryByShortCountryName("US");
				Province pro = provinceMapper.getSnByName(order.getOrder().getCprovince(), coun.getIid());
				String provincename = pro==null ? order.getOrder().getCprovince() : pro.getCshortname();
				// 省份
				params.put("PAYMENTREQUEST_0_SHIPTOSTATE", provincename);
			}else{
				// 省份
				params.put("PAYMENTREQUEST_0_SHIPTOSTATE", order.getOrder()
						.getCprovince());
			}
			
			Logger.debug("params=====province==={}",params.get("PAYMENTREQUEST_0_SHIPTOSTATE").toString());
			
			// 邮编
			params.put("PAYMENTREQUEST_0_SHIPTOZIP", order.getOrder()
					.getCpostalcode());
			// 国家代码
			params.put("PAYMENTREQUEST_0_SHIPTOCOUNTRYCODE", order.getOrder()
					.getCcountrysn());
			// 电话号码
			params.put("PAYMENTREQUEST_0_SHIPTOPHONENUM", order.getOrder()
					.getCtelephone());

			// 立即支付
			// params.put("PAYMENTREQUEST_0_ALLOWEDPAYMENTMETHOD",
			// "InstantPaymentOnly");
		}
		return params;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * services.paypal.IExpressCheckoutNvpService#setExpressCheckout(java.lang
	 * .String, java.lang.String, java.lang.String)
	 */
	public PaypalNvpPaymentStatus setExpressCheckout(SetExpressCheckout set,
			WebContext webCtx) {

		
		String orderNum = set.getOrderNum();
		String returnUrl = set.getReturnUrl();
		String cancelUrl = set.getCancalUrl();

		this.assertConfig();

		if (orderNum == null || orderNum.length() == 0) {
			throw new NullPointerException("orderNum can not null");
		}
		if (returnUrl == null || returnUrl.length() == 0) {
			throw new NullPointerException("returnUrl can not null");
		}
		if (cancelUrl == null || cancelUrl.length() == 0) {
			throw new NullPointerException("cancelUrl can not null");
		}
		// 更新订单的cpaymentid字段,该字段标示用户默认的支付方式是paypal
		this.updateService.updatePaymentIdByOrderNum("paypal", orderNum);

		int siteId = foundation.getSiteID();
		// 该过程的状态信息
		PaypalNvpPaymentStatus status = new PaypalNvpPaymentStatus();
		status.setStage(PaypalNvpPaymentStatus.STAGE_SET);
		status.setOrderNum(orderNum);
		status.setWebSiteId(siteId);

		// 先检查订单是否已经支付过
		boolean isAlreadyPaid = this.orderService.isAlreadyPaid(orderNum);
		if (isAlreadyPaid) {
			StringBuilder sb = new StringBuilder();
			sb.append("youer order:");
			sb.append(orderNum);
			sb.append(" already paid");
			status.setFailedInfo(sb.toString());
			return status;
		}
		// this.assertConfig();
		Map<String, String> params = this.createTransactionDetails(orderNum);
		//lang
		//LOCALECODE=fr_FR
		String lang = foundation.getLang();
		if(lang != null){
			params.put("LOCALECODE", lang);
		}
		
		// 是否让用户留言
		if (set.isAllowNote()) {
			params.put("ALLOWNOTE", "1");
		} else {
			// 不让用户留言
			params.put("ALLOWNOTE", "0");
		}
		// 是否可用用paypal中的地址
		if (set.isUsePaypalShipping()) {
			params.put("NOSHIPPING", "2");
		} else {
			params.put("NOSHIPPING", "1");
		}
		// 展示shipping address 但是不能修改
		if (set.isAddroverride()) {
			params.put("ADDROVERRIDE", "1");
		} else {
			params.put("ADDROVERRIDE", "0");
		}

		params.put("METHOD", "SetExpressCheckout");
		params.put("RETURNURL", returnUrl);
		params.put("CANCELURL", cancelUrl);
		params.put("PAYMENTREQUEST_0_INVNUM", orderNum);

//		String notifyUrl = controllers.paypal.routes.PayPalPayment
//				.returnFormPayment().absoluteURL(Context.current().request());
		
		String notifyUrl = getIpnUrl();

		params.put("NOTIFYURL", notifyUrl);

		Logger.debug("set paypal notifyurl:{}", notifyUrl);

		String orderId = params.get(TT_ORDER_ID);
		params.remove(TT_ORDER_ID);

		Logger.debug("params-*-**-*--*-*-{}",params);
		WS.client()
				.url(this.endPoint)
				.post(this.toUrlEncoded(params))
				.map(resp -> {

					String payload = resp.getBody().toString();
					HashMap<String, String> response = this
							.deformatNVP(payload);

					status.setPaypalReply(response);

					Logger.debug("paypal setExpressCheckout response:{}",
							response);
					// 设置这个参数是为了后面记录日志时能区分出日志时哪个阶段产生的
					response.put("customStage",
							PaypalNvpPaymentStatus.STAGE_SET);

					if (response.containsKey("ACK")) {
						String ack = response.get("ACK");
						if ("Success".equals(ack)) {
							String token = response.get("TOKEN");
							if (set.isEc()) {
								status.setRedirectURL(this.redirectURL + token
										+ "&useraction=continue");
							} else {
								status.setRedirectURL(this.redirectURL + token
										+ "&useraction=commit");
							}
							status.setNextStep(true);
						}
					}
					if (response.containsKey("L_ERRORCODE0")) {
						status.setErrorCode(response.get("L_ERRORCODE0"));
					}
					if (response.containsKey("L_LONGMESSAGE0")) {
						status.setFailedInfo(response.get("L_LONGMESSAGE0"));
					}
					// 发送事件去记录日志
					Map<Object, Object> paras = Maps.newLinkedHashMap();
					paras.putAll(response);
					// 参数记录到日志中
					paras.put("paras", params);

					ObjectMapper objectMapper = new ObjectMapper();
					JSONObject json = objectMapper.convertValue(paras,
							JSONObject.class);
					PaypaiReturnLog log = new PaypaiReturnLog(orderId, json
							.toString());
					log.setIwebsiteid(siteId);
					eventBus.post(log);
					return status;
				}).get(TIMEOUT);
		return status;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * services.paypal.IExpressCheckoutNvpService#GetExpressCheckoutDetails(
	 * java.lang.String, java.lang.String)
	 */
	public Map<String, String> GetExpressCheckoutDetails(String token,
			String PayerID, String orderNum) {
		Map<String, String> params = Maps.newHashMap();

		Order order = orderMapper.getOrderByOrderNumber(orderNum);

		this.setSecurity(params, order);

		params.put("METHOD", "GetExpressCheckoutDetails");
		params.put("TOKEN", token);
		params.put("PAYERID", PayerID);

		return WS
				.client()
				.url(this.endPoint)
				.post(this.toUrlEncoded(params))
				.map(resp -> {

					String payload = resp.getBody().toString();
					HashMap<String, String> response = this
							.deformatNVP(payload);

					Logger.debug(
							"paypal getExpressCheckoutDetails response:{}",
							response);

					return response;
				}).get(TIMEOUT);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * services.paypal.IExpressCheckoutNvpService#DoExpressCheckoutPayment(java
	 * .lang.String, java.lang.String, java.lang.String)
	 */
	public PaypalNvpPaymentStatus DoExpressCheckoutPayment(String token,
			String PayerID, String orderNum) {
		this.assertConfig();

		// 该过程的状态信息
		PaypalNvpPaymentStatus status = new PaypalNvpPaymentStatus();
		status.setStage(PaypalNvpPaymentStatus.STAGE_DO);

		Logger.debug("orderNum:{}", orderNum);
		Map<String, String> params = this.createTransactionDetails(orderNum);
		params.put("METHOD", "DoExpressCheckoutPayment");
		params.put("TOKEN", token);
		params.put("PAYERID", PayerID);

		String notifyUrl = getIpnUrl();

		params.put("PAYMENTREQUEST_0_NOTIFYURL", notifyUrl);
		Logger.debug("do paypal notifyurl:{}", notifyUrl);

		String orderId = params.get(TT_ORDER_ID);
		params.remove(TT_ORDER_ID);

		boolean dropShip = params.get(DROP_SHIP) != null ? true : false;
		params.remove(DROP_SHIP);

		int siteId = this.foundation.getSiteID();
		int lang = this.foundation.getLanguage();

		status.setOrderNum(orderNum);
		status.setWebSiteId(siteId);

		WS.client()
				.url(this.endPoint)
				.post(this.toUrlEncoded(params))
				.map(resp -> {

					String payload = resp.getBody().toString();
					HashMap<String, String> response = this
							.deformatNVP(payload);

					Logger.warn("paypal doExpressCheckoutPayment response:{}",
							response);

					status.setPaypalReply(response);

					String transactionId = null;

					if (response.containsKey("PAYMENTINFO_0_ACK")) {
						String ack = response.get("PAYMENTINFO_0_ACK");
						if ("success".equals(ack.toLowerCase())) {
							String paymentStatus = response
									.get("PAYMENTINFO_0_PAYMENTSTATUS");
							status.setCompleted(true);
							status.setPaymentStatus(paymentStatus);
							// 发出事件去更新订单的付款状态
							transactionId = response
									.get("PAYMENTINFO_0_TRANSACTIONID");
							Logger.debug("paypal transactionId:{}",
									transactionId);
							String state = StatusTransformer
									.transformPaypal(paymentStatus);
							String ttAccount = null;
							if (params.containsKey(TT_ACCOUNT)) {
								ttAccount = params.get(TT_ACCOUNT);
							}
							String amt = response.get("PAYMENTINFO_0_AMT");


							if (IOrderStatusService.PAYMENT_CONFIRMED
									.equals(state)) {
								// 发事件修改库存
								if (dropShip) {
									List<String> orderIDs = dropShippingMapEnquiry
											.getOrderNumbersByID(orderNum);

									for (String id : orderIDs) {
										paymentEnquiry.confirmPayment(id,
												transactionId, lang);
									}

								} else {
									Order order = orderMapper
											.getOrderByOrderId(Integer
													.parseInt(orderId));
									List<OrderDetail> details = enquiryService
											.getOrderDetails(Integer
													.parseInt(orderId));
									eventBus.post(new PaymentConfirmationEvent(
											new OrderValue(order, details),
											transactionId, lang));
								}

							}

						}
					}

					if (response.containsKey("L_ERRORCODE0")) {
						status.setErrorCode(response.get("L_ERRORCODE0"));
					}
					if (response.containsKey("L_LONGMESSAGE0")) {
						status.setFailedInfo(response.get("L_LONGMESSAGE0"));
					}

					// 发送事件去记录日志
					Map<Object, Object> paras = Maps.newLinkedHashMap();
					paras.putAll(response);
					// 参数记录到日志中
					paras.put("paras", params);

					ObjectMapper objectMapper = new ObjectMapper();

					JSONObject json = objectMapper.convertValue(paras,
							JSONObject.class);
					PaypaiReturnLog log = new PaypaiReturnLog(orderId, json
							.toString());
					log.setIwebsiteid(siteId);
					log.setCtransactionid(transactionId);
					eventBus.post(log);
					return status;

				}).get(TIMEOUT);
		return status;
	}

	/**
	 * encode key-value pair to UTF-8
	 * 
	 * @param params
	 * @return
	 */
	private String toUrlEncoded(Map<String, String> params) {
		return URLEncodedUtils.format(
				FluentIterable
						.from(params.entrySet())
						.transform(
								e -> new BasicNameValuePair(e.getKey(), e
										.getValue())).toList(), "UTF-8");
	}

	/**
	 * break the NVP string into a HashMap
	 * 
	 * @param payload
	 * @return a HashMap object containing all the name value pairs of the
	 *         string.
	 */
	private HashMap<String, String> deformatNVP(String payload) {
		HashMap<String, String> nvp = Maps.newHashMap();
		StringTokenizer stTok = new StringTokenizer(payload, "&");
		while (stTok.hasMoreTokens()) {
			StringTokenizer stInternalTokenizer = new StringTokenizer(
					stTok.nextToken(), "=");
			if (stInternalTokenizer.countTokens() == 2) {
				try {
					String key = URLDecoder.decode(
							stInternalTokenizer.nextToken(), "UTF-8");
					String value = URLDecoder.decode(
							stInternalTokenizer.nextToken(), "UTF-8");
					nvp.put(key.toUpperCase(), value);
				} catch (UnsupportedEncodingException e) {
					Logger.debug("deformatNVP error", e);
				}

			}
		}
		return nvp;
	}

	@Override
	public boolean saveShipAddress(String token, String PayerID, String orderNum) {
		Map<String, String> result = this.GetExpressCheckoutDetails(token,
				PayerID, orderNum);

		Order originalOrder = orderMapper.getOrderByOrderNumber(orderNum);
		// 填充订单shipping地址
		Order order = new Order();
		order.setCordernumber(orderNum);
		// 国家
		String countryCode = result.get("SHIPTOCOUNTRYCODE");
		String country = result.get("SHIPTOCOUNTRYNAME");
		order.setCcountry(country);
		order.setCcountrysn(countryCode);
		// email
		String email = result.get("EMAIL");
		order.setCemail(email);
		if (originalOrder.getCmemberemail() == null) {
			order.setCmemberemail(email);
		}
		// 省份
		String shipToState = result.get("SHIPTOSTATE");
		order.setCprovince(shipToState);
		// city
		String shipToCity = result.get("SHIPTOCITY");
		order.setCcity(shipToCity);
		// street
		String shipTosTreet = result.get("SHIPTOSTREET");
		String shipTosTreet2 = result.get("SHIPTOSTREET2");
		order.setCstreetaddress(shipTosTreet
				+ (shipTosTreet2 == null ? "" : " " + shipTosTreet2));
		// 邮编
		String shipToZip = result.get("SHIPTOZIP");
		order.setCpostalcode(shipToZip);
		// shiptoname
		String shipToName = result.get("SHIPTONAME");
		// 把名称拆成first last
		if (shipToName != null) {
			shipToName = shipToName.trim();
			int index = shipToName.lastIndexOf(" ");
			if (index != -1) {
				order.setCfirstname(shipToName.substring(0, index));
				order.setClastname(shipToName.substring(index + 1));
			} else {
				order.setCfirstname(shipToName);
			}
		}

		// String currencycode = result.get("CURRENCYCODE");
		// Currency currency = currencyService.getCurrencyByCode(currencycode);

		// FluentIterable.from(result.keySet()).forEach(k -> {
		// Logger.debug("{}:{}",k,result.get(k));
		// });

		// IOrderFragment fragment = shipMethod.getFragment(n, countryCode);
		// Html shipMethodHtml = shipMethodRenderer.render(fragment, currency);
		// Logger.debug(shipMethodHtml.toString());
		// ShippingMethodInformations shipInfo =
		// (ShippingMethodInformations)fragment;
		// Integer shipId = shipInfo.getList().get(0).getId();
		// order.setIshippingmethodid(shipId);
		// //运费
		// Double freight = shipInfo.getList().get(0).getFreight();
		// //重新计算总价格
		// order.setFshippingprice(freight);

		return orderService.updateShipAddressAndShipPrice(order);
	}

	private String getIpnUrl(){
		String notifyUrl = Play.application().configuration().getString("ipn.paypal");
		if(notifyUrl == null || notifyUrl.length() == 0){
			throw new NullPointerException("config:ipn.paypal not find");
		}
		return notifyUrl;
	}
}
