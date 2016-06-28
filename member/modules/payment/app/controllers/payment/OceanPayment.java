package controllers.payment;

import java.io.ByteArrayInputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.beanutils.BeanUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import play.Logger;
import play.data.Form;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Result;
import services.IWebsiteService;
import services.order.IOrderEnquiryService;
import services.order.IOrderStatusService;
import services.order.TotalOrderService;
import services.order.dropShipping.DropShippingMapEnquiryService;
import services.order.dropShipping.DropShippingUpdateService;
import services.order.payment.PaymentCallbackService;
import services.payment.IPaymentConfirmationService;
import services.payment.OceanPaymentService;
import valueobjects.payment.OceanPaymentResult;

import com.google.common.collect.Maps;
import com.google.common.eventbus.EventBus;

import controllers.order.PaymentControllerBase;
import dto.order.Order;
import dto.order.OrderDetail;
import dto.order.PaymentCallback;
import dto.order.dropShipping.DropShipping;
import events.payment.PaymentEvent;

public class OceanPayment extends PaymentControllerBase {

	@Inject
	private OceanPaymentService paymentService;
	@Inject
	private IPaymentConfirmationService paymentEnquiry;
	@Inject
	private PaymentCallbackService callbackService;
	@Inject
	private IOrderEnquiryService orderEnquiry;
	@Inject
	private DropShippingMapEnquiryService dropShippingMapEnquiry;
	@Inject
	private DropShippingUpdateService dropShippingUpdate;
	@Inject
	private TotalOrderService totalOrderService;
	@Inject
	private EventBus eventBus;
	@Inject
	private IWebsiteService websiteService;

	@SuppressWarnings("unchecked")
	public Result userPOST() {
		Form<OceanPaymentResult> form = Form.form(OceanPaymentResult.class)
				.bindFromRequest();
		if (form.hasErrors()) {
			return badRequest("form error: " + form.errorsAsJson());
		}
		OceanPaymentResult result = form.get();
		Map<String, String> resultMap = Form.form().bindFromRequest().get()
				.getData();
		String orderId = result.getOrder_number();
		Logger.debug("OceanPayment userPOST orderID: {}, Result: {}", orderId,
				result);
		String signValue = paymentService.getOceanValidSignValue(result,
				orderId);
		PaymentCallback pc = new PaymentCallback();
		pc.setCcontent("userPOST: " + Json.toJson(resultMap).toString());
		pc.setCordernumber(orderId);
		
		if (signValue.toLowerCase().equals(result.getSignValue().toLowerCase())) {
			Order order = orderEnquiry.getOrderById(orderId);
			if (order == null) {
				pc.setCresponse("badRequest::can not found order with orderID:"
						+ orderId);
				callbackService.insert(pc);
				return badRequest("can not found order with orderID: "
						+ orderId);
			}
			pc.setCpaymentid(order.getCpaymentid());
			pc.setIwebsiteid(order.getIwebsiteid());
			if ("1".equals(result.getPayment_status())) {
				return redirectToPaymentConfirmation(orderId);
			} else if ("-1".equals(result.getPayment_status())) {
				return redirectToPaymentConfirmation(orderId);
			}
		}
		pc.setCresponse("redirectToPayFail");
		callbackService.insert(pc);
		return redirectToPayFail(orderId, result.toString());
	}

	@BodyParser.Of(BodyParser.Raw.class)
	public Result serverPOST() {
		Logger.debug("serverPOST+++++");
		byte[] raw = request().body().asRaw().asBytes();
		String contentType = request().getHeader("Content-Type");
		PaymentCallback pc = new PaymentCallback();
		if (raw != null) {
			pc.setCcontent("OceanPayment serverPOST Result: ["
					+ new String(raw) + "], content type: [" + contentType
					+ "]");
			Logger.debug(
					"OceanPayment serverPOST Result: [{}], content type: [{}]",
					new String(raw), contentType);
		}
		OceanPaymentResult result = new OceanPaymentResult();
		Document dc = buildDocument(new String(raw));
		if (null != dc) {
			Map<String, String> map = Maps.newHashMap();
			NodeList nodeList = dc.getElementsByTagName("response").item(0)
					.getChildNodes();
			for (int i = 0; i < nodeList.getLength(); i++) {
				Node node = nodeList.item(i);
				if (node.getNodeType() == 1) {
					map.put(node.getNodeName(), node.getTextContent());
				}
			}
			try {
				BeanUtils.populate(result, map);
			} catch (IllegalAccessException | InvocationTargetException e) {
				Logger.error("BeanUtils populate map to result: ", e);
			}
		}
		if (null == result.getOrder_number() || null == result.getSignValue()) {
			pc.setCresponse("badRequest::order_number is null or signValue is null");
			callbackService.insert(pc);
			return badRequest("order_number is null or signValue is null");
		}
		String orderId = result.getOrder_number();
		pc.setCordernumber(orderId);
		String signValue = paymentService.getOceanValidSignValue(result,
				orderId);
		if (signValue == null) {
			pc.setCresponse("badRequest::can not found order with orderID:"
					+ orderId);
			callbackService.insert(pc);
			return badRequest("not found order: " + orderId);
		}
		Logger.debug("serverPOST+++++result===={}",result);
		Logger.debug("serverPOST+++++signValue===={}",signValue);
		if (signValue.toLowerCase().equals(result.getSignValue().toLowerCase())) {
			Order order = orderEnquiry.getOrderById(orderId);
			if (order == null) {
				pc.setCresponse("badRequest::can not found order with orderID:"
						+ orderId);
				callbackService.insert(pc);
				return badRequest("can not found order with orderID: "
						+ orderId);
			}
			pc.setCpaymentid(order.getCpaymentid());
			pc.setIwebsiteid(order.getIwebsiteid());
			if ("1".equals(result.getPayment_status())) {
				String transactionId = result.getPayment_id();
				updateOrderStatus(orderId, transactionId,
						IOrderStatusService.PAYMENT_CONFIRMED);
				pc.setCresponse("ok");
				callbackService.insert(pc);
				// 支付成功传递赠送积分事件
				List<OrderDetail> detailsPoint = orderEnquiry
						.getOrderDetails(orderId);
				if (null != order) {
					Integer webisteId = order.getIwebsiteid();
					Integer ilanguageid = websiteService.getWebsite(webisteId)
							.getIlanguageid();
					eventBus.post(new events.order.PaymentConfirmationEvent(
							new valueobjects.order_api.OrderValue(order,
									detailsPoint), result.getPayment_id(),
							ilanguageid));
				}

				Logger.debug(
						"------------------------------>server call paypent confirmed,ordernum={}",
						order.getCordernumber());
				
				//订单成功，更改订单paymentstatus状态
				PaymentEvent event = new PaymentEvent(result.getOrder_number(),
						IOrderStatusService.PAYMENT_CONFIRMED , transactionId, result.getAccount() , result.getOrder_amount(),
						order.getCpaymentid());
				eventBus.post(event);
				
				return ok("receive-ok");
			} else if ("-1".equals(result.getPayment_status())) {
				Logger.debug("serivcepost ====> result.getPayment_status()==-1");
				String transactionId = result.getPayment_id();
				updateOrderStatus(orderId, transactionId,
						IOrderStatusService.PAYMENT_PROCESSING);
				pc.setCresponse("ok");
				callbackService.insert(pc);
				//订单padding中，预授权中，更改订单paymentstatus状态
				PaymentEvent event = new PaymentEvent(result.getOrder_number(),
						IOrderStatusService.PAYMENT_PENDING , transactionId, result.getAccount() , result.getOrder_amount(),
						order.getCpaymentid());
				eventBus.post(event);
				
				return ok();
			} else if ("0".equals(result.getPayment_status())) {
				Logger.debug("serivcepost ====> result.getPayment_status()==0");
				pc.setCresponse("ok");
				pc.setCresponse("faile");
				callbackService.insert(pc);
				return ok();
			}
		}
		Logger.error(
				"order signValue error {}  mysignValue {} othersignValue {}",
				orderId, signValue, result.getSignValue());
		return badRequest();
	}

	private void updateOrderStatus(String orderId, String transactionId,
			String status) {
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
					if (status != null) {
						paymentEnquiry
								.confirmPayment(id, transactionId, status);
					} else {
						paymentEnquiry.confirmPayment(id, transactionId);
					}
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
					if (status != null) {
						paymentEnquiry.confirmPayment(order.getCordernumber(),
								transactionId, status);
					} else {
						paymentEnquiry.confirmPayment(order.getIid(),
								transactionId);
					}
				}
			}
		} else {
			if (status != null) {
				paymentEnquiry.confirmPayment(orderId, transactionId, status);
			} else {
				paymentEnquiry.confirmPayment(orderId, transactionId);
			}
		}
	}

	protected Document buildDocument(String xml) {
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			return builder.parse(new ByteArrayInputStream(xml.getBytes()));
		} catch (Exception e) {
			Logger.error("XML Parsing Error", e);
			return null;
		}
	}
}
