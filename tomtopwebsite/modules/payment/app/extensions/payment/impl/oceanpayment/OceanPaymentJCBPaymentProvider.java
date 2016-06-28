package extensions.payment.impl.oceanpayment;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import mapper.order.PaymentAccountMapper;
import play.Configuration;
import play.Logger;
import play.Play;
import play.data.DynamicForm;
import play.mvc.Http.Context;
import play.twirl.api.Html;
import services.ICountryService;
import services.base.CurrencyService;
import services.base.FoundationService;
import services.base.SystemParameterService;
import services.base.utils.DoubleCalculateUtils;
import services.base.utils.JsonFormatUtils;
import services.base.utils.StringUtils;
import services.member.address.AddressService;
import services.member.login.LoginService;
import services.order.OrderUpdateService;
import services.payment.OceanPaymentService;
import valueobjects.order_api.payment.PaymentContext;

import com.google.common.collect.Maps;

import dto.Country;
import dto.member.MemberAddress;
import dto.order.Order;
import dto.order.OrderDetail;
import enums.OrderLableEnum;
import extensions.payment.IPaymentProvider;

public class OceanPaymentJCBPaymentProvider implements IPaymentProvider {

	@Inject
	PaymentAccountMapper accountMapper;

	@Inject
	OceanPaymentService paymentService;

	@Inject
	CurrencyService currencyService;

	@Inject
	FoundationService foundation;

	@Inject
	LoginService loginService;

	@Inject
	AddressService addressService;

	@Inject
	ICountryService countryService;

	@Inject
	OrderUpdateService updateService;
	
	@Inject
	SystemParameterService systemParameterService;

	@Override
	public String id() {
		return "oceanpayment_jcb";
	}

	@Override
	public String name() {
		return "JCB";
	}

	@Override
	public int getDisplayOrder() {
		return 201;
	}

	@Override
	public String iconUrl() {
		return controllers.base.routes.Assets.at("images/newshopping_icon_jcb.png").url();
	}

	@Override
	public Html getDescription(PaymentContext context) {
		String email = loginService.getLoginData().getEmail();
		MemberAddress orderAddress;
		if (context != null && context.getBillID() != null) {
			orderAddress = addressService.getMemberAddressById(context
					.getBillID());
		} else {
			orderAddress = addressService.getDefaultOrderAddress(email);
		}
//		if (null == orderAddress) {
//			orderAddress = addressService.getDefaultShippingAddress(email);
//		}
		// List<Country> countryList = countryService.getReallyAllCountries();
		// Map<Integer, Country> countryMap = Maps.uniqueIndex(countryList,
		// e -> e.getIid());
		// modify by lijun
		if (orderAddress != null) {
			Country country = countryService.getCountryByCountryId(orderAddress
					.getIcountry());
			if (null != country) {
				orderAddress.setCountryFullName(country.getCname());
				orderAddress.setCountryCode(country.getCshortname());
			}
		}

		if (context == null || context.isModeNew()) {
			return views.html.payment.oceanpayment.v2.jbc_credit_description
					.render(orderAddress);
		} else {
			List<Country> countryList = countryService.getReallyAllCountries();
			Map<Integer, Country> countryMap = Maps.uniqueIndex(countryList,
					e -> e.getIid());
			return views.html.payment.oceanpayment.JBC_description.render(
					orderAddress, countryList, countryMap);
		}

	}

	@Override
	public boolean isManualProcess() {
		return false;
	}

	@Override
	public Html getInstruction(PaymentContext context) {
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Html getDoPaymentHtml(PaymentContext context) {
		Order order = context.getOrder().getOrder();
		String ccy = foundation.getCurrency();
		double usdgrandprice = currencyService.exchange(order.getFgrandtotal(), ccy, "USD");
		String paymentid = order.getCpaymentid();	//支付方式
		String accountString = accountMapper.getAccount(order.getIwebsiteid(),
				usdgrandprice,paymentid);
		if (StringUtils.isEmpty(accountString)) {
			return null;
		}
		LinkedHashMap<String, String> account = JsonFormatUtils.jsonToBean(
				accountString, LinkedHashMap.class);
		MemberAddress address = addressService.getMemberAddressById(Integer
				.valueOf(context.getMap().get("billAddressId")));
		LinkedHashMap<String, String> form = parseToForm(order, account,
				address, context.getOrder().getDetails(), context.getBackUrl());
		Configuration config = Play.application().configuration()
				.getConfig("oceanpayment");
		boolean isSandbox = config.getBoolean("sandbox");
		updatePaymentAccount(context, account.get("account"));
		return views.html.payment.oceanpayment.oceandetail.render(form,
				isSandbox);
	}

	private void updatePaymentAccount(PaymentContext context, String account) {
		if (OrderLableEnum.DROP_SHIPPING.getName().equals(
				context.getOrderLable())) {
			updateAccountTotal(context, account);
		} else if (OrderLableEnum.TOTAL_ORDER.getName().equals(
				context.getOrderLable())) {
			updateAccountTotal(context, account);
		} else {
			updateAccountDefault(context, account);
		}
	}

	private void updateAccountDefault(PaymentContext context, String account) {
		updateService.updatePaymentAccount(context.getOrder().getOrder()
				.getIid(), account);
	}

	private void updateAccountTotal(PaymentContext context, String account) {
		List<OrderDetail> details = context.getOrder().getDetails();
		for (OrderDetail detail : details) {
			updateService.updatePaymentAccount(detail.getIorderid(), account);
		}
	}

	private LinkedHashMap<String, String> parseToForm(Order order,
			LinkedHashMap<String, String> account, MemberAddress address,
			List<OrderDetail> list, String backUrl) {
		LinkedHashMap<String, String> form = Maps.newLinkedHashMap();
		Country country = countryService.getCountryByCountryId(address
				.getIcountry());
		if (StringUtils.isEmpty(backUrl)) {
			backUrl = controllers.payment.routes.OceanPayment.userPOST()
					.absoluteURL(Context.current().request());
		}
		Logger.debug("Back URL: {}", backUrl);
//		String noticeUrl = controllers.payment.routes.OceanPayment.serverPOST()
//				.absoluteURL(Context.current().request());
		
		String noticeUrl = Play.application().configuration().getString("ipn.ocean");
		if(noticeUrl == null || noticeUrl.length() == 0){
			throw new NullPointerException("config:ipn.ocean not find");
		}
		
		Logger.debug("Notice URL: {}", noticeUrl);
		Map<String, String> productMap = paymentService
				.getOceanProductMap(list);

		form.put("account", account.get("account"));
		form.put("terminal", account.get("terminal"));

		form.put("order_number", order.getCordernumber());
		form.put("order_currency", order.getCcurrency());
		form.put("order_amount", String.valueOf(new DoubleCalculateUtils(order
				.getFgrandtotal()).doubleValue()));

		form.put("methods", "Credit Card");

		form.put("backUrl", backUrl);
		form.put("noticeUrl", noticeUrl);

		form.put("billing_firstName", address.getCfirstname());
		form.put("billing_lastName", address.getClastname());
		form.put("billing_email", address.getCmemberemail());
		form.put("billing_phone", address.getCtelephone());
		form.put("billing_country", country.getCshortname());
		form.put("billing_state", address.getCprovince());
		form.put("billing_city", address.getCcity());
		form.put("billing_address", address.getCstreetaddress());
		form.put("billing_zip", address.getCpostalcode());

		form.put("ship_firstName", order.getCfirstname());
		form.put("ship_lastName", order.getClastname());
		form.put("ship_phone", order.getCtelephone());
		form.put("ship_country", order.getCcountrysn());
		form.put("ship_state", order.getCprovince());
		form.put("ship_city", order.getCcity());
		form.put("ship_addr", order.getCstreetaddress());
		form.put("ship_zip", order.getCpostalcode());

		form.put("productSku", productMap.get("productSku"));
		form.put("productName", productMap.get("productName"));
		form.put("productNum", productMap.get("productNum"));

		form.put("cart_api", "V1.6.0");

		form.put(
				"signValue",
				paymentService.getOceanPostSignValue(form,
						account.get("secureCode")));
		return form;
	}

	@Override
	public boolean isNeedExtraInfo() {
		return true;
	}

	@Override
	public boolean validForm(DynamicForm df) {
		if (StringUtils.isEmpty(df.get("billAddressId"))) {
			df.reject("billAddressId", "is null");
			return false;
		}
		return true;
	}

	@Override
	public String area() {
		return "GLOBAL";
	}
}
