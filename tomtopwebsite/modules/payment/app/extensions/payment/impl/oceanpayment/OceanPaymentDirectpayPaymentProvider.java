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

public class OceanPaymentDirectpayPaymentProvider implements IPaymentProvider {

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
		return "oceanpayment_directpay";
	}

	@Override
	public String name() {
		return "Directpay";
	}

	@Override
	public int getDisplayOrder() {
		return 800;
	}

	@Override
	public String iconUrl() {
		return controllers.base.routes.Assets.at("images/Directpay.png").url();
	}

	@Override
	public Html getDescription(PaymentContext context) {
		if (context == null || context.isModeNew()) {
			return views.html.payment.oceanpayment.v2.webmoney_credit_description
					.render(this.iconUrl());
		} else {
			return views.html.payment.oceanpayment.webmoney_description.render(this.iconUrl());
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
		LinkedHashMap<String, String> form = parseToForm(order, account,
				context.getOrder().getDetails(), context.getBackUrl());
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
			LinkedHashMap<String, String> account,
			List<OrderDetail> list, String backUrl) {
		LinkedHashMap<String, String> form = Maps.newLinkedHashMap();
		if (StringUtils.isEmpty(backUrl)) {
			backUrl = controllers.payment.routes.OceanPayment.userPOST()
					.absoluteURL(Context.current().request());
		}
		Logger.debug("Back URL: {}", backUrl);
		String noticeUrl = controllers.payment.routes.OceanPayment.serverPOST()
				.absoluteURL(Context.current().request());
		Logger.debug("Notice URL: {}", noticeUrl);
		Map<String, String> productMap = paymentService
				.getOceanProductMap(list);

		form.put("account", account.get("account"));
		form.put("terminal", account.get("terminal"));

		form.put("order_number", order.getCordernumber());
		form.put("order_currency", order.getCcurrency());
		form.put("order_amount", String.valueOf(new DoubleCalculateUtils(order
				.getFgrandtotal()).doubleValue()));

		form.put("methods", "Directpay");

		form.put("backUrl", backUrl);
		form.put("noticeUrl", noticeUrl);

		form.put("billing_firstName", order.getCfirstname());
		form.put("billing_lastName", order.getClastname());
		form.put("billing_email", order.getCemail());
		form.put("billing_phone", order.getCtelephone());
		form.put("billing_country", order.getCcountrysn());
		form.put("billing_state", order.getCprovince());
		form.put("billing_city", order.getCcity());
		form.put("billing_address", order.getCstreetaddress());
		form.put("billing_zip", order.getCpostalcode());

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
		return true;
	}

	@Override
	public String area() {
		return "GLOBAL";
	}
}
