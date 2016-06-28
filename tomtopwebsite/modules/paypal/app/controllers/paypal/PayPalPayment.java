package controllers.paypal;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.io.IOUtils;

import play.Configuration;
import play.Logger;
import play.Play;
import play.data.Form;
import play.libs.F.Promise;
import play.libs.Json;
import play.libs.ws.WS;
import play.libs.ws.WSRequestHolder;
import play.mvc.BodyParser;
import play.mvc.Result;
import services.IFoundationService;
import services.base.utils.DoubleCalculateUtils;
import services.order.IOrderEnquiryService;
import services.order.IOrderStatusService;
import services.order.TotalOrderService;
import services.order.dropShipping.DropShippingMapEnquiryService;
import services.order.dropShipping.DropShippingUpdateService;
import services.payment.PaymentConfirmationService;
import services.paypal.PaymentServices;

import com.google.common.collect.ListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimaps;

import controllers.order.PaymentControllerBase;
import dto.order.Order;
import dto.order.dropShipping.DropShipping;
import entity.payment.PaypaiReturn;

public class PayPalPayment extends PaymentControllerBase {

	@Inject
	private PaymentServices paymentServices;
	@Inject
	private IFoundationService foundationService;
	@Inject
	private PaymentConfirmationService paymentEnquiry;
	@Inject
	private DropShippingMapEnquiryService dropShippingMapEnquiry;
	@Inject
	private DropShippingUpdateService dropShippingUpdate;
	@Inject
	private IOrderEnquiryService orderEnquiry;
	@Inject
	private TotalOrderService totalOrderService;

	@Inject
	IOrderStatusService service;
	
	public final static String SOURCE = "paypal";

	@BodyParser.Of(BodyParser.Raw.class)
	public Promise<Result> returnFormPayment() {
		// paypal seems need raw content
		byte[] contents = request().body().asRaw().asBytes();

		// parse form data on our own
		Map<String, String[]> formFields = parseRawContent(contents);

		Form<PaypaiReturn> form = Form.form(PaypaiReturn.class)
				.bindFromRequest(formFields);
		PaypaiReturn result = form.get();
		int siteId = 1;
		try {
			siteId = foundationService.getSiteID(foundationService
					.getWebContext());
		} catch (Exception e) {
			Logger.error("paypal return payment error can not get siteId: ", e);
		}
		Logger.debug("order id:{}", result.getInvoice());
		Logger.debug("payment status :{}", result.getPayment_status());
		paymentServices.InsertLog(siteId, result.getInvoice(),
				Json.toJson(result).toString());
		if (result.getInvoice() == null
				|| result.getMc_gross() == null
				|| !verifyOrderTotal(result.getInvoice(),
						Double.valueOf(result.getMc_gross()))) {
			return Promise.pure(ok("Return with a wrong total"));
		}
		if ("Completed".equals(result.getPayment_status())) {
			String orderId = result.getInvoice();
			Order order = orderEnquiry.getOrderById(orderId);
			if (order == null) {
				return Promise.pure(badRequest());
			}
			Promise<String> pverified = verifyIPN(contents);
			return pverified.map(verified -> {
				if ("VERIFIED".equals(verified)) {
					updateOrderStatus(orderId, result);
				}
				return ok(verified);
			});
		}
		return Promise.pure(ok("ok"));
	}

	private void updateOrderStatus(String orderId, PaypaiReturn result) {
		//modify by lijun
		service.changeOrdePaymentStatus(orderId, 1);
		if (orderId.endsWith("-DS")) {
			List<String> orderIDs = dropShippingMapEnquiry
					.getOrderNumbersByID(orderId);
			if (orderIDs.isEmpty()) {
				Logger.debug(
						"PayPalPayment updateOrderStatus can not found order with orderID: {}",
						orderId);
			} else {
				DropShipping ds = new DropShipping();
				ds.setBpaid(true);
				ds.setCdropshippingid(orderId);
				dropShippingUpdate.updateByDropShippingID(ds);
				for (String id : orderIDs) {
					paymentEnquiry.confirmPayment(id, result.getTxn_id(),
							this.foundationService
									.getLanguage(this.foundationService
											.getWebContext()));
					service.changeOrdePaymentStatus(id, 1);
				}
			}
		} else if (orderId.endsWith("-TT")) {
			List<Order> orders = totalOrderService.getOrdersByTotalCID(orderId);
			if (orders.isEmpty()) {
				Logger.debug(
						"PayPalPayment updateOrderStatus can not found order with orderID: {}",
						orderId);
			} else {
				for (Order order : orders) {
					paymentEnquiry.confirmPayment(order.getCordernumber(),
							result.getTxn_id(), this.foundationService
									.getLanguage(this.foundationService
											.getWebContext()));
				}
			}
		} else {
			paymentEnquiry.confirmPayment(orderId, result.getTxn_id(),
					this.foundationService.getLanguage(this.foundationService
							.getWebContext()));// 传入交易号
		}
	}

	private boolean verifyOrderTotal(String orderID, double returnTotal) {
		double orderTotal = 0.0;
		if (orderID.endsWith("-DS")) {
			List<String> orderIDs = dropShippingMapEnquiry
					.getOrderNumbersByID(orderID);
			if (orderIDs.isEmpty()) {
				Logger.error("can not found order with orderID: " + orderID);
				return false;
			} else {
				for (String id : orderIDs) {
					Order order = orderEnquiry.getOrderById(id);
					if (order != null) {
						orderTotal += order.getFgrandtotal();
					}
				}
			}
		} else {
			Order order = orderEnquiry.getOrderById(orderID);
			if (order != null) {
				orderTotal = order.getFgrandtotal();
			}
		}
		DoubleCalculateUtils dcu = new DoubleCalculateUtils(orderTotal);
		Double resTotal = returnTotal;
		if (dcu.subtract(resTotal).doubleValue() > 0.01
				|| dcu.subtract(resTotal).doubleValue() < -0.01) {
			Logger.debug(
					"Wrong Total OrderID: {}, Order Total: {}, Payment Total: {}",
					orderID, orderTotal, resTotal);
			return false;
		}
		return true;
	}

	private Map<String, String[]> parseRawContent(byte[] contents) {
		try {
			String data = new String(contents, "UTF-8");
			List<String> pv = Lists.newArrayList(data.split("&"));
			ListMultimap<String, String> pmm = Multimaps.index(pv,
					p -> p.split("=")[0]);
			ListMultimap<String, String> pmmDecoded = Multimaps
					.transformValues(
							pmm,
							p -> {
								if (p != null) {
									String[] split = p.split("=");
									if (split.length > 1) {
										try {
											return URLDecoder.decode(split[1],
													"UTF-8");
										} catch (Exception e) {
											Logger.error(
													"Parameter Parsing Error, {}",
													p);
											return "";
										}
									}
								}
								return "";
							});
			return Maps.transformValues(pmmDecoded.asMap(), cv -> Lists
					.newArrayList(cv).toArray(new String[cv.size()]));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public Promise<String> verifyIPN(byte[] formData) {
		Configuration config = Play.application().configuration()
				.getConfig("paypal");
		String url = "";
		if (config != null) {
			Boolean sandbox = config.getBoolean("sandbox");
			if (sandbox != null && sandbox) {
				url = "https://www.sandbox.paypal.com/cgi-bin/webscr";
			} else {
				url = "https://www.paypal.com/cgi-bin/webscr";
			}
		}

		try {
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			os.write(formData);
			os.write("&cmd=_notify-validate".getBytes());
			byte[] concat = os.toByteArray();
			ByteArrayInputStream is = new ByteArrayInputStream(concat);
			Logger.debug("paypal payment check form: {}", new String(concat));
			Logger.debug("Size: {}", concat.length);
			File tmp = File.createTempFile("paypal-validateform", ".form");
			IOUtils.copy(is, new FileOutputStream(tmp));
			WSRequestHolder wh = WS.client().url(url)
					.setContentType("application/x-www-form-urlencoded");
			Promise<String> p = wh.post(tmp).map(res -> {
				if (tmp != null)
					tmp.delete();
				if (res.getBody() == null) {
					Logger.debug("Body is NULL");
					return "";
				}
				Logger.debug("paypal payment check result: {}", res.getBody());
				return res.getBody();
			});
			return p;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

}
