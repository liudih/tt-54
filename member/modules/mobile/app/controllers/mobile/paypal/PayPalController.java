package controllers.mobile.paypal;

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
import services.base.utils.DoubleCalculateUtils;
import services.mobile.MobileService;
import services.order.IOrderEnquiryService;
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

public class PayPalController extends PaymentControllerBase {

	@Inject
	private PaymentServices paymentServices;
	@Inject
	private MobileService mobileService;
	@Inject
	private PaymentConfirmationService paymentEnquiry;
	@Inject
	private DropShippingMapEnquiryService dropShippingMapEnquiry;
	@Inject
	private DropShippingUpdateService dropShippingUpdate;
	@Inject
	private IOrderEnquiryService orderEnquiry;

	@BodyParser.Of(BodyParser.Raw.class)
	public Promise<Result> returnFormPayment() {

		// paypal seems need raw content
		byte[] contents = request().body().asRaw().asBytes();

		// parse form data on our own
		Map<String, String[]> formFields = parseRawContent(contents);

		Form<PaypaiReturn> form = Form.form(PaypaiReturn.class)
				.bindFromRequest(formFields);
		PaypaiReturn result = form.get();
		paymentServices.InsertLog(mobileService.getWebSiteID(),
				result.getInvoice(), Json.toJson(result).toString());
		if (!verifyOrderTotal(result.getInvoice(),
				Double.valueOf(result.getMc_gross()))) {
			return Promise.pure(badRequest("Return with a wrong total"));
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
					if (orderId.endsWith("-DS")) {
						List<String> orderIDs = dropShippingMapEnquiry
								.getOrderNumbersByID(orderId);
						if (orderIDs.isEmpty()) {
							return ok("can not found order with orderID: "
									+ orderId);
						} else {
							DropShipping ds = new DropShipping();
							ds.setBpaid(true);
							ds.setCdropshippingid(orderId);
							dropShippingUpdate.updateByDropShippingID(ds);
							for (String id : orderIDs) {
								paymentEnquiry.confirmPayment(id,
										result.getTxn_id(),
										mobileService.getLanguageID());
							}
						}
					} else {
						paymentEnquiry.confirmPayment(orderId,
								result.getTxn_id(),
								mobileService.getLanguageID());// 传入交易号
				}
			}
			return ok(verified);
		})	;
		}
		return Promise.pure(ok("ok"));
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
		ByteArrayOutputStream os = null;
		ByteArrayInputStream is = null;
		FileOutputStream fos = null;
		try {
			os = new ByteArrayOutputStream();
			os.write(formData);
			os.write("&cmd=_notify-validate".getBytes());
			byte[] concat = os.toByteArray();
			is = new ByteArrayInputStream(concat);
			Logger.debug("paypal payment check form: {}", new String(concat));
			Logger.debug("Size: {}", concat.length);
			File tmp = File.createTempFile("paypal-validateform", ".form");
			fos = new FileOutputStream(tmp);
			IOUtils.copy(is, fos);
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
		} finally {
			try {
				if (null != os) {
					os.close();
				}
				if (null != is) {
					is.close();
				}
				if (null != fos) {
					fos.close();
				}
			} catch (IOException e) {
				Logger.error("PayPalController verifyIPN IOException", e);
			}
		}
	}
}
