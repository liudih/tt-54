package controllers.api;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import play.Logger;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Results;
import services.base.ReceivedDataHistoryService;
import services.base.utils.StringUtils;
import services.base.receivedata.HandleReceivedDataType;
import services.order.IOrderEnquiryService;
import services.order.IOrderStatusService;
import services.order.OrderPackService;
import services.order.OrderUpdateService;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;

import controllers.annotation.ApiHistory;
import dto.ReceivedDataHistory;
import dto.order.OrderIdStatusId;
import dto.order.OrderPack;
import forms.order.MemberOrderForm;

public class Order extends Controller {

	@Inject
	OrderUpdateService orderUpdateService;

	@Inject
	IOrderEnquiryService orderEnquiryService;

	@Inject
	IOrderStatusService orderStatusService;

	@Inject
	OrderPackService orderPackService;

	@Inject
	ReceivedDataHistoryService receivedDataHistoryService;

	@BodyParser.Of(BodyParser.Json.class)
	public Result push() {

		String user = request().getHeader("user-token");
		JsonNode jnode = request().body().asJson();
		if (null == jnode) {
			return internalServerError("Expecting Json data");
		}
		String re = "";
		if (jnode.isArray()) {
			com.website.dto.order.Order[] orders = Json.fromJson(jnode,
					com.website.dto.order.Order[].class);
			re = orderUpdateService.saveBatch(orders);
		} else {
			com.website.dto.order.Order order = Json.fromJson(jnode,
					com.website.dto.order.Order.class);
			re = orderUpdateService
					.saveBatch(new com.website.dto.order.Order[] { order });
		}
		if (re == null || re.trim().length() == 0) {
			return ok("successfully");
		} else {
			return internalServerError(re);
		}
	}

	/**
	 * 
	 * @param pageSize
	 *            分页的大小
	 * @param pageNum
	 *            页数
	 * @param start
	 * @param end
	 * @return Json.toJson(orderList)
	 * @author lijia
	 */
	@JsonGetter
	public Result getOrder() {
		ObjectMapper mapper = new ObjectMapper();
		Map result = mapper.convertValue(request().body().asJson(), Map.class);
		if (result.get("createuser") == null
				|| !"erp".equals(result.get("createuser"))) {
			return internalServerError("error request");
		}

		Integer pageNum = 0;
		if (result.get("pageNum") == null) {
			return internalServerError("pageNum is empty");
		} else {
			try {
				pageNum = Integer.parseInt(result.get("pageNum").toString());
			} catch (NumberFormatException e) {
				return internalServerError("pageNum: " + e.toString());
			}
		}

		Integer pageSize = 0;
		if (result.get("pageSize") == null) {
			return internalServerError("pageSize is empty");
		} else {
			try {
				pageSize = Integer.parseInt(result.get("pageSize").toString());
			} catch (NumberFormatException e) {
				return internalServerError("pageSize: " + e.toString());
			}
		}

		String start1 = result.get("start").toString();
		String end1 = result.get("end").toString();
		Date start = null;
		Date end = null;
		if (start1 != "") {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			try {
				start = sdf.parse(start1);
			} catch (ParseException e) {
				start = null;

				return internalServerError("start 日期格式错误");
			}
		}

		if (end1 != "") {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			try {
				end = sdf.parse(end1);
			} catch (ParseException e) {
				e.printStackTrace();

				return internalServerError("end 日期格式错误");
			}
		}
		MemberOrderForm form = new MemberOrderForm();
		form.setPageNum(pageNum);
		form.setPageSize(pageSize);
		// 订单状态为收款确认
		Integer orderStatus = orderStatusService
				.getIdByName(IOrderStatusService.PAYMENT_CONFIRMED);
		form.setStatus(orderStatus);
		Collection<com.website.dto.order.Order> orders = orderEnquiryService
				.getOrders(form, start, end);
		if (orders.size() > 0) {
			return ok(Json.toJson(orders));
		} else {
			return ok("");
		}
	}

	/**
	 * 
	 * @param pageSize
	 * @param pageNum
	 * @param start
	 * @param end
	 * @return Json.toJson(Integer) 当前时间查询条件下的订单总数
	 * @author lijia
	 */
	public Result getOrderAllTotal() {
		ObjectMapper mapper = new ObjectMapper();
		Map result = mapper.convertValue(request().body().asJson(), Map.class);
		String start1 = result.get("start").toString();
		String end1 = result.get("end").toString();
		Date start = null;
		Date end = null;
		if (start1 != "") {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			try {
				start = sdf.parse(start1);
			} catch (ParseException e) {
				start = null;

				return internalServerError("start 日期格式错误");
			}
		}

		if (end1 != "") {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			try {
				end = sdf.parse(end1);
			} catch (ParseException e) {
				e.printStackTrace();

				return internalServerError("end 日期格式错误");
			}
		}
		MemberOrderForm form = new MemberOrderForm();
		Integer orderStatus = orderStatusService
				.getIdByName(IOrderStatusService.PAYMENT_CONFIRMED);
		form.setStatus(orderStatus);
		Integer allTotal = orderEnquiryService.searchOrderCount(form, start,
				end);
		Map<String, Integer> allTotalMap = new HashMap<String, Integer>();
		allTotalMap.put("count", allTotal);

		return ok(Json.toJson(allTotalMap));
	}

	/**
	 * 
	 * @param orderId
	 * @return Order
	 * @author lijia
	 */
	public Result getOrderByOrderId(String orderNumber) {
		Integer orderId = orderEnquiryService
				.getOrderIdByOrderNumber(orderNumber);
		if (orderId == null) {
			String msg = "not find this orderNumber-" + orderNumber;
			Logger.info("getOrderByOrderId is error: " + msg);
			return internalServerError(msg);
		}
		com.website.dto.order.Order order = orderEnquiryService
				.getOrderByOrderId(orderId);
		if (order != null) {
			return ok(Json.toJson(order));
		} else {
			return ok("");
		}
	}

	/**
	 * 修改订单状态为订单处理中
	 * 
	 * @param orderId
	 * @return boolean
	 * @author lijia
	 */
	@JsonGetter
	public Result changeOrderStatusToProcessing() {
		JsonNode jnode = request().body().asJson();
		if (null == jnode || !jnode.has("createuser")
				|| !jnode.has("orderNumber")) {
			Logger.info("changeOrderStatusToProcessing is error: Expecting Json data"
					+ jnode);
			return internalServerError("Expecting Json data");
		}
		String createuser = jnode.get("createuser").asText();
		if (!"erp".equals(createuser)) {
			Logger.info("error request!!" + jnode);
			return internalServerError("Expecting Json data");
		}
		String orderNumber = jnode.get("orderNumber").asText();
		OrderIdStatusId orderIdStatusId = orderEnquiryService
				.getOrderIdStatusByOrderNumber(orderNumber);
		if (orderIdStatusId == null) {
			String msg = "not find this orderNumber-" + orderNumber;
			Logger.info("changeOrderStatusToProcessing is error: " + msg);
			return internalServerError(msg);
		}
		Integer orderId = orderIdStatusId.getIorderid();
		String nowStatus = orderStatusService
				.getOrderStatusNameById(orderIdStatusId.getIstatus());
		// 保存erp推送数据
		ReceivedDataHistory receivedDataHistory = new ReceivedDataHistory();
		String handletype = HandleReceivedDataType.UPDATE_ORDER_STATUS
				.getType();
		String content = "change orderNumber " + orderNumber + " status form "
				+ nowStatus + " to " + IOrderStatusService.PROCESSING;
		receivedDataHistory.setCcontent(content);
		receivedDataHistory.setCtype(handletype);
		receivedDataHistory.setCcreateuser("erp");
		receivedDataHistoryService.addReceivedDataHistory(receivedDataHistory);
		boolean changeOrdeStaus = orderStatusService.changeOrdeStatus(orderId,
				IOrderStatusService.PROCESSING);

		Map<String, Boolean> result = new HashMap<String, Boolean>();
		result.put("result", changeOrdeStaus);
		Logger.info("changeOrderStatusToProcessing: orderId is " + orderId
				+ ", result is " + changeOrdeStaus);
		return ok(Json.toJson(result));
	}

	/**
	 * 修改订单状态为已发货
	 * 
	 * @param orderId
	 * @return boolean
	 * @author lijia
	 */
	@ApiHistory(type = HandleReceivedDataType.UPDATE_ORDER_PACK, createuser = "api")
	@JsonGetter
	public Result changeOrderStatusToDispatched() {
		try {
			JsonNode jnode = request().body().asJson();
			if (null == jnode || !jnode.has("data")
					|| !jnode.has("orderNumber")) {
				Logger.info("changeOrderStatusToDispatched is error: Expecting Json data"
						+ jnode);
				return internalServerError("Expecting Json data");
			}
			String orderNumber = jnode.get("orderNumber").asText();
			OrderIdStatusId orderIdStatusId = orderEnquiryService
					.getOrderIdStatusByOrderNumber(orderNumber);
			if (null == orderIdStatusId) {
				String msg = "not find this orderNumber-" + orderNumber;
				Logger.info(msg);
				return internalServerError(msg);
			}
			JsonNode orderPackData = jnode.get("data");
			boolean savePackData = false;
			Integer nowStatusId = orderIdStatusId.getIstatus();
			Integer orderId = orderIdStatusId.getIorderid();
			if (orderPackData.isNull()) {
				savePackData = true;
			} else {
				if (orderPackData.isArray()) {
					OrderPack[] orderPacks = Json.fromJson(orderPackData,
							OrderPack[].class);
					List<OrderPack> orderPackList = Arrays.asList(orderPacks);
					orderPackList = Lists.transform(orderPackList,
							orderPack -> {
								orderPack.setIorderid(orderId);
								return orderPack;
							});

					savePackData = orderPackService
							.batchInsertOrderPack(orderPackList);
				} else {
					OrderPack orderPack = Json.fromJson(orderPackData,
							OrderPack.class);
					orderPack.setIorderid(orderId);
					savePackData = orderPackService.insertOrderPack(orderPack);
				}
			}

			String nowStatus = orderStatusService
					.getOrderStatusNameById(nowStatusId);
			if (savePackData
					&& (IOrderStatusService.PAYMENT_CONFIRMED.equals(nowStatus)
							|| IOrderStatusService.ON_HOLD.equals(nowStatus) || IOrderStatusService.PROCESSING
								.equals(nowStatus))) {
				// 保存追踪号后,修改订单状态(之后会拆分)
				ReceivedDataHistory receivedDataHistory = new ReceivedDataHistory();
				String handletype = HandleReceivedDataType.UPDATE_ORDER_STATUS
						.getType();
				String content = "change orderNumber " + orderNumber
						+ " status form " + nowStatus + " to "
						+ IOrderStatusService.COMPLETED;
				receivedDataHistory.setCcontent(content);
				receivedDataHistory.setCtype(handletype);
				receivedDataHistory.setCcreateuser("api");
				receivedDataHistoryService
						.addReceivedDataHistory(receivedDataHistory);
				boolean changeOrdeStaus = orderStatusService.changeOrdeStatus(
						orderId, IOrderStatusService.DISPATCHED);
				Map<String, Boolean> result = new HashMap<String, Boolean>();
				result.put("result", changeOrdeStaus);
				Logger.info("save order pack is success, data : "
						+ orderPackData);
				Logger.info("changeOrderStatusToDispatched: orderId is "
						+ orderId + ", result is " + changeOrdeStaus);
				return ok(Json.toJson(result));
			} else if (savePackData) {
				Map<String, Boolean> result = new HashMap<String, Boolean>();
				result.put("result", savePackData);
				Logger.info("save order pack is success, data : "
						+ orderPackData);
				return ok(Json.toJson(result));
			} else {
				Map<String, Boolean> result = new HashMap<String, Boolean>();
				result.put("result", savePackData);
				Logger.info("save order pack is fair, data : " + orderPackData);
				return badRequest(Json.toJson(result));
			}
		} catch (Exception ex) {
			Logger.error("erp update order error: ", ex);
			return Results.internalServerError("update order error: "
					+ ex.getMessage());
		}
	}

	/**
	 * 修改订单状态为已完成
	 * 
	 * @param orderId
	 * @return boolean
	 * @author lijia
	 */
	@JsonGetter
	public Result changeOrderStatusToCompleted() {
		JsonNode jnode = request().body().asJson();
		if (null == jnode || !jnode.has("createuser")
				|| !jnode.has("orderNumber")) {
			Logger.info("changeOrderStatusToCompleted is error: Expecting Json data"
					+ jnode);
			return internalServerError("Expecting Json data");
		}
		String createuser = jnode.get("createuser").asText();
		if (!"erp".equals(createuser)) {
			Logger.info("error request!!" + jnode);
			return internalServerError("Expecting Json data");
		}
		String orderNumber = jnode.get("orderNumber").asText();
		OrderIdStatusId orderIdStatusId = orderEnquiryService
				.getOrderIdStatusByOrderNumber(orderNumber);
		if (orderIdStatusId == null) {
			String msg = "not find this orderNumber-" + orderNumber;
			Logger.info("changeOrderStatusToCompleted is error: " + msg);
			return internalServerError(msg);
		}
		Integer orderId = orderIdStatusId.getIorderid();
		String nowStatus = orderStatusService
				.getOrderStatusNameById(orderIdStatusId.getIstatus());
		// 保存erp推送数据
		ReceivedDataHistory receivedDataHistory = new ReceivedDataHistory();
		String handletype = HandleReceivedDataType.UPDATE_ORDER_STATUS
				.getType();
		String content = "change orderNumber " + orderNumber + " status form "
				+ nowStatus + " to " + IOrderStatusService.COMPLETED;
		receivedDataHistory.setCcontent(content);
		receivedDataHistory.setCtype(handletype);
		receivedDataHistory.setCcreateuser("api");
		receivedDataHistoryService.addReceivedDataHistory(receivedDataHistory);
		boolean changeOrdeStaus = orderStatusService.changeOrdeStatus(orderId,
				IOrderStatusService.COMPLETED);

		Map<String, Boolean> result = new HashMap<String, Boolean>();
		result.put("result", changeOrdeStaus);
		Logger.info("changeOrderStatusToCompleted: orderId is " + orderId
				+ ", result is " + changeOrdeStaus);
		return ok(Json.toJson(result));
	}

	/**
	 * 修改订单状态为On Hold
	 * 
	 * @param orderId
	 * @return boolean
	 * @author lijia
	 */
	@JsonGetter
	public Result changeOrderStatusToOnHold() {
		JsonNode jnode = request().body().asJson();
		if (null == jnode || !jnode.has("createuser")
				|| !jnode.has("orderNumber")) {
			Logger.info("changeOrderStatusToOnHold is error: Expecting Json data"
					+ jnode);
			return internalServerError("Expecting Json data");
		}
		String createuser = jnode.get("createuser").asText();
		if (!"erp".equals(createuser)) {
			Logger.info("error request!!" + jnode);
			return internalServerError("Expecting Json data");
		}
		String orderNumber = jnode.get("orderNumber").asText();
		OrderIdStatusId orderIdStatusId = orderEnquiryService
				.getOrderIdStatusByOrderNumber(orderNumber);
		if (orderIdStatusId == null) {
			String msg = "not find this orderNumber-" + orderNumber;
			Logger.info("changeOrderStatusToOnHold is error: " + msg);
			return internalServerError(msg);
		}
		Integer orderId = orderIdStatusId.getIorderid();
		String nowStatus = orderStatusService
				.getOrderStatusNameById(orderIdStatusId.getIstatus());
		// 保存erp推送数据
		ReceivedDataHistory receivedDataHistory = new ReceivedDataHistory();
		String handletype = HandleReceivedDataType.UPDATE_ORDER_STATUS
				.getType();
		String content = "change orderNumber " + orderNumber + " status form "
				+ nowStatus + " to " + IOrderStatusService.ON_HOLD;
		receivedDataHistory.setCcontent(content);
		receivedDataHistory.setCtype(handletype);
		receivedDataHistory.setCcreateuser("api");
		receivedDataHistoryService.addReceivedDataHistory(receivedDataHistory);
		boolean changeOrdeStaus = orderStatusService.changeOrdeStatus(orderId,
				IOrderStatusService.ON_HOLD);
		Map<String, Boolean> result = new HashMap<String, Boolean>();
		result.put("result", changeOrdeStaus);
		Logger.info("changeOrderStatusToOnHold: orderId is " + orderId
				+ ", result is " + changeOrdeStaus);
		return ok(Json.toJson(result));
	}

	/**
	 * 修改订单状态为Refunded
	 * 
	 * @param orderId
	 * @return boolean
	 * @author lijia
	 */
	@JsonGetter
	public Result changeOrderStatusToRefunded() {
		JsonNode jnode = request().body().asJson();
		if (null == jnode || !jnode.has("createuser")
				|| !jnode.has("orderNumber")) {
			Logger.info("changeOrderStatusToRefunded is error: Expecting Json data"
					+ jnode);
			return internalServerError("Expecting Json data");
		}
		String createuser = jnode.get("createuser").asText();
		if (!"erp".equals(createuser)) {
			Logger.info("error request!!" + jnode);
			return internalServerError("Expecting Json data");
		}
		String orderNumber = jnode.get("orderNumber").asText();
		OrderIdStatusId orderIdStatusId = orderEnquiryService
				.getOrderIdStatusByOrderNumber(orderNumber);
		if (orderIdStatusId == null) {
			String msg = "not find this orderNumber-" + orderNumber;
			Logger.info("changeOrderStatusToRefunded is error: " + msg);
			return internalServerError(msg);
		}
		Integer orderId = orderIdStatusId.getIorderid();
		String nowStatus = orderStatusService
				.getOrderStatusNameById(orderIdStatusId.getIstatus());
		// 保存erp推送数据
		ReceivedDataHistory receivedDataHistory = new ReceivedDataHistory();
		String handletype = HandleReceivedDataType.UPDATE_ORDER_STATUS
				.getType();
		String content = "change orderNumber " + orderNumber + " status form "
				+ nowStatus + " to " + IOrderStatusService.REFUNDED;
		receivedDataHistory.setCcontent(content);
		receivedDataHistory.setCtype(handletype);
		receivedDataHistory.setCcreateuser("api");
		receivedDataHistoryService.addReceivedDataHistory(receivedDataHistory);
		boolean changeOrdeStaus = orderStatusService.changeOrdeStatus(orderId,
				IOrderStatusService.REFUNDED);
		Map<String, Boolean> result = new HashMap<String, Boolean>();
		result.put("result", changeOrdeStaus);
		Logger.info("changeOrderStatusToRefunded: orderId is " + orderId
				+ ", result is " + changeOrdeStaus);
		return ok(Json.toJson(result));
	}

	@ApiHistory(type = HandleReceivedDataType.UPDATE_ORDER_REMARK, createuser = "api")
	@BodyParser.Of(BodyParser.Json.class)
	public Result updateOrderRemark() {
		JsonNode jnode = request().body().asJson();

		Map<String, String> result = new HashMap<String, String>();
		if (null == jnode || !jnode.has("orderNumber") || !jnode.has("remark")) {
			result.put("error", "Expecting Json data");
			return internalServerError(Json.toJson(result));
		}
		String orderNumber = jnode.get("orderNumber").asText();
		Integer orderId = orderEnquiryService
				.getOrderIdByOrderNumber(orderNumber);
		if (orderId == null) {
			String msg = "not find this orderNumber-" + orderNumber;
			Logger.info("updateOrderRemark is error: " + msg);
			return internalServerError(msg);
		}
		String remark = jnode.get("remark").asText();
		if (remark.length() > 500 || orderId == 0
				|| StringUtils.isEmpty(remark)) {
			result.put("error", "the data is error");
			return internalServerError(Json.toJson(result));
		}
		boolean updateOrderRemark = orderUpdateService.updateOrderRemark(
				orderId, remark);
		if (updateOrderRemark) {
			result.put("result", "success");
		} else {
			result.put("error", "false");
			return internalServerError(Json.toJson(result));
		}

		return ok(Json.toJson(result));
	}
}
