package handlers.order;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import play.Logger;
import play.twirl.api.Html;
import services.base.CountryService;
import services.base.CurrencyService;
import services.base.utils.DateFormatUtils;
import services.base.EmailAccountService;
import services.base.EmailTemplateService;
import services.order.IOrderEnquiryService;
import services.order.IOrderStatusService;
import services.order.PostEmailOrderService;
import services.shipping.IShippingMethodService;
import base.util.mail.EmailUtil;

import com.google.common.collect.Maps;
import com.google.common.eventbus.Subscribe;

import email.model.EmailType;
import email.util.EmailSpreadUtil;
import email.valueobjects.NonPaymentFields;
import dto.EmailAccount;
import dto.ShippingMethodDetail;
import dto.order.Order;
import dto.order.OrderDetail;
import events.order.RemindPaymentEvent;

public class RemindPaymentHandler {
	@Inject
	IOrderEnquiryService enquiryService;
	@Inject
	IOrderStatusService statusService;
	@Inject
	PostEmailOrderService postEmailOrderService;
	@Inject
	EmailTemplateService emailTemplateService;
	@Inject
	EmailAccountService emailAccountService;
	@Inject
	CurrencyService currencyService;
	@Inject
	IShippingMethodService shippingMethodService;
	@Inject
	CountryService countryEnquiryService;
	@Inject
	EmailSpreadUtil emailSpread;

	@Subscribe
	public void remindPayment(RemindPaymentEvent event) {
		try {
			Integer status = statusService
					.getIdByName(IOrderStatusService.PAYMENT_PENDING);
			List<Integer> orderIds = postEmailOrderService
					.findUnpostEmailOrder(status);
			for (Integer orderId : orderIds) {
				Order order = enquiryService.getOrderById(orderId);
				if (postEmail(order)) {
					postEmailOrderService.insert(order);
				}
			}
		} catch (Exception e) {
			Logger.error("remindPayment error: ", e);
		}
	}

	private Boolean postEmail(Order order) {
		Integer language = 1;
		Integer websiteid = countryEnquiryService
				.getLanguageByShortCountryName(order.getCcountrysn());
		EmailAccount emailaccount = emailAccountService
				.getEmailAccount(websiteid);
		List<OrderDetail> details = enquiryService.getOrderDetails(order
				.getIid());
		String address = order.getCstreetaddress() + " " + order.getCcity()
				+ " " + order.getCprovince() + " " + order.getCcountry();
		String date = DateFormatUtils.getInstance("EEE, d MMMM yyyy").getDate(
				order.getDcreatedate());
		String symbol = currencyService.getCurrencyByCode(order.getCcurrency())
				.getCsymbol();
		String shippingMethod = "";
		ShippingMethodDetail smd = shippingMethodService
				.getShippingMethodDetail(order.getIshippingmethodid(), language);
		if (null != smd) {
			shippingMethod = smd.getCtitle();
		}
		Html tempHtml = views.html.order.email.non_payment_tr.render(details,
				symbol);
		String items = tempHtml.body();

		NonPaymentFields nonPaymentFields = new NonPaymentFields(
				EmailType.NON_PAYMENT.getType(), language);

		nonPaymentFields.setAddress(address);
		nonPaymentFields.setDate(date);
		nonPaymentFields.setFirstName(order.getCfirstname());
		nonPaymentFields.setFreight(Double.valueOf(order.getFshippingprice())
				.toString());
		nonPaymentFields.setGrandTotal(Double.valueOf(order.getFgrandtotal())
				.toString());
		nonPaymentFields.setItems(items);
		nonPaymentFields.setOrderId(order.getCordernumber());
		nonPaymentFields.setShippingMethod(shippingMethod);
		nonPaymentFields.setSubtotal(Double.valueOf(order.getFordersubtotal())
				.toString());
		nonPaymentFields.setSymbol(symbol);
		nonPaymentFields.setUrl(controllers.order.routes.OrderProcessing
				.replaceOrder(order.getCordernumber(), null).url());

		String title = "";
		String content = "";
		Map<String, String> titleAndContentMap = Maps.newHashMap();
		try {
			titleAndContentMap = emailTemplateService.getEmailContent(
					nonPaymentFields, order.getIwebsiteid());
			if (null != titleAndContentMap && titleAndContentMap.size() > 0) {
				title = titleAndContentMap.get("title");
				content = titleAndContentMap.get("content");
			} else {
				Logger.error("title and content is null ,can not send email");
				return false;
			}
		} catch (Exception e) {
			Logger.error("can not deal with verify email content");
			e.printStackTrace();
		}
		if (emailaccount == null) {
			Logger.info("sendEmail email server account null!");
			return false;
		}
		//return EmailUtil.send(title, content, emailaccount, order.getCemail());
		return emailSpread.send(emailaccount.getCemail(),
				order.getCemail(), title, content);
	}
}
