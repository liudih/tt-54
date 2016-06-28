package com.rabbit.services.serviceImp.rabbitproduct.api;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.rabbit.common.enums.HandleReceivedDataType;
import com.rabbit.dto.order.MemberOrderForm;
import com.rabbit.dto.order.OrderBack;
import com.rabbit.dto.order.OrderIdStatusId;
import com.rabbit.dto.order.OrderPack;
import com.rabbit.dto.order.ReceivedDataHistory;
import com.rabbit.services.iservice.order.IOrderEnquiryService;
import com.rabbit.services.iservice.order.IOrderPackService;
import com.rabbit.services.iservice.order.IOrderStatusService;
import com.rabbit.services.iservice.shipping.IShippingMethodService;
import com.rabbit.services.serviceImp.order.ReceivedDataHistoryService;

@Service
public class OrderApi {
	private static final Logger log=Logger.getLogger(OrderApi.class.getName());

	@Autowired
	IOrderEnquiryService orderEnquiryService;
	@Autowired
	IOrderStatusService orderStatusService;
	@Autowired
	IOrderPackService orderPackService;
	@Autowired
	ReceivedDataHistoryService receivedDataHistoryService;
	@Autowired
	private IShippingMethodService shippingMethodService;
	
	/**
	 * 
	 * @param pageSize
	 * @param pageNum
	 * @param start
	 * @param end
	 * @return Json.toJson(Integer) 当前时间查询条件下的订单总数
	 * @author lijia
	 * @throws Exception 
	 */
	@SuppressWarnings("rawtypes")
	public String getOrderAllTotal( String jsonParam) throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		Map result = mapper.convertValue(jsonParam, Map.class);
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

				throw new Exception("start 日期格式错误");
			}
		}

		if (end1 != "") {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			try {
				end = sdf.parse(end1);
			} catch (ParseException e) {
				e.printStackTrace();

				throw new Exception("end 日期格式错误");
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

		return JSON.toJSONString(allTotalMap);
	}
	/**
	 * 
	 * @param orderId
	 * @return Order
	 * @author lijia
	 * @throws Exception 
	 */
	public String getOrderByOrderId(String orderNumber) throws Exception {
		Integer orderId = orderEnquiryService
				.getOrderIdByOrderNumber(orderNumber);
		if (orderId == null) {
			String msg = "not find this orderNumber-" + orderNumber;
			log.info("getOrderByOrderId is error: " + msg);
			throw new Exception(msg);
		}
		OrderBack order = orderEnquiryService
				.getOrderByOrderId(orderId);
		if (order != null) {
			return JSON.toJSONString(order);
		} else {
			return "";
		}
	}
	/**
	 * 修改订单状态为Refunded
	 * 
	 * @param orderId
	 * @return boolean
	 * @author lijia
	 * @throws Exception 
	 */
	public String changeOrderStatusToRefunded(JsonNode jnode) throws Exception {
		if (null == jnode || !jnode.has("createuser")
				|| !jnode.has("orderNumber")) {
			log.info("changeOrderStatusToRefunded is error: Expecting Json data"
					+ jnode);
			throw new Exception("Expecting Json data");
		}
		String createuser = jnode.get("createuser").asText();
		if (!"erp".equals(createuser)) {
			log.info("error request!!" + jnode);
			throw new Exception("Expecting Json data");
		}
		String orderNumber = jnode.get("orderNumber").asText();
		OrderIdStatusId orderIdStatusId = orderEnquiryService
				.getOrderIdStatusByOrderNumber(orderNumber);
		if (orderIdStatusId == null) {
			String msg = "not find this orderNumber-" + orderNumber;
			log.info("changeOrderStatusToRefunded is error: " + msg);
			throw new Exception(msg);
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
		log.info("changeOrderStatusToRefunded: orderId is " + orderId
				+ ", result is " + changeOrdeStaus);
		return JSON.toJSONString(result);
	}
	/**
	 * 修改订单状态为订单处理中
	 * 
	 * @param orderId
	 * @return boolean
	 * @author lijia
	 * @throws Exception 
	 */
	public String changeOrderStatusToProcessing(JsonNode jnode) throws Exception {
		if (null == jnode || !jnode.has("createuser")
				|| !jnode.has("orderNumber")) {
			log.info("changeOrderStatusToProcessing is error: Expecting Json data"
					+ jnode);
			throw new Exception("Expecting Json data");
		}
		String createuser = jnode.get("createuser").asText();
		if (!"erp".equals(createuser)) {
			log.info("error request!!" + jnode);
			throw new Exception("Expecting Json data");
		}
		String orderNumber = jnode.get("orderNumber").asText();
		OrderIdStatusId orderIdStatusId = orderEnquiryService
				.getOrderIdStatusByOrderNumber(orderNumber);
		if (orderIdStatusId == null) {
			String msg = "not find this orderNumber-" + orderNumber;
			log.info("changeOrderStatusToProcessing is error: " + msg);
			throw new Exception(msg);
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
		log.info("changeOrderStatusToProcessing: orderId is " + orderId
				+ ", result is " + changeOrdeStaus);
		return JSON.toJSONString(result);
	}

	/**
	 * 修改订单状态为On Hold
	 * 
	 * @param orderId
	 * @return boolean
	 * @author lijia
	 * @throws Exception 
	 */
	public String changeOrderStatusToOnHold(JsonNode jnode ) throws Exception {
		if (null == jnode || !jnode.has("createuser")
				|| !jnode.has("orderNumber")) {
			log.info("changeOrderStatusToOnHold is error: Expecting Json data"
					+ jnode);
			throw new Exception("Expecting Json data");
		}
		String createuser = jnode.get("createuser").asText();
		if (!"erp".equals(createuser)) {
			log.info("error request!!" + jnode);
			throw new Exception("Expecting Json data");
		}
		String orderNumber = jnode.get("orderNumber").asText();
		OrderIdStatusId orderIdStatusId = orderEnquiryService
				.getOrderIdStatusByOrderNumber(orderNumber);
		if (orderIdStatusId == null) {
			String msg = "not find this orderNumber-" + orderNumber;
			log.info("changeOrderStatusToOnHold is error: " + msg);
			throw new Exception(msg);
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
		log.info("changeOrderStatusToOnHold: orderId is " + orderId
				+ ", result is " + changeOrdeStaus);
		return JSON.toJSONString(result);
	}
	/**
	 * 修改订单状态为已完成
	 * 
	 * @param orderId
	 * @return boolean
	 * @author lijia
	 * @throws Exception 
	 */
	
	public String changeOrderStatusToCompleted(JsonNode jnode) throws Exception {
		if (null == jnode || !jnode.has("createuser")
				|| !jnode.has("orderNumber")) {
			log.info("changeOrderStatusToCompleted is error: Expecting Json data"
					+ jnode);
			throw new Exception("Expecting Json data");
		}
		String createuser = jnode.get("createuser").asText();
		if (!"erp".equals(createuser)) {
			log.info("error request!!" + jnode);
			 throw new Exception("Expecting Json data");
		}
		String orderNumber = jnode.get("orderNumber").asText();
		OrderIdStatusId orderIdStatusId = orderEnquiryService
				.getOrderIdStatusByOrderNumber(orderNumber);
		if (orderIdStatusId == null) {
			String msg = "not find this orderNumber-" + orderNumber;
			log.info("changeOrderStatusToCompleted is error: " + msg);
			throw new Exception(msg);
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
		log.info("changeOrderStatusToCompleted: orderId is " + orderId
				+ ", result is " + changeOrdeStaus);
		return JSON.toJSONString(result);
	}
	public String changeOrderStatusToDispatched(String jsonParam) throws Exception {
		if(StringUtils.isEmpty(jsonParam)){
			 log.info("getProductsByListingid jsonParam empty");
			 return "[]";
		}
		ObjectMapper om=new ObjectMapper();
		JsonNode jnode = om.readTree(jsonParam);
		
		if (null == jnode || !jnode.has("data") || !jnode.has("orderNumber")) {
			log.info("changeOrderStatusToDispatched is error: Expecting Json data"
					+ jnode);
			throw new  Exception("Expecting Json data");
		}
		String orderNumber = jnode.get("orderNumber").asText();
		OrderIdStatusId orderIdStatusId = orderEnquiryService
				.getOrderIdStatusByOrderNumber(orderNumber);
		if (null == orderIdStatusId) {
			String msg = "not find this orderNumber-" + orderNumber;
			log.info(msg);
			throw new  Exception(msg);
		}
		JsonNode orderPackData = jnode.get("data");
		boolean savePackData = false;
		Integer nowStatusId = orderIdStatusId.getIstatus();
		Integer orderId = orderIdStatusId.getIorderid();
		if (orderPackData.isNull()) {
			savePackData = true;
		} else {
			if (orderPackData.isArray()) {
				/*OrderPack[] orderPacks =JSON.parseArray(orderPackData.asText(), OrderPack[].class);*/
				OrderPack[] orderPacks = om.convertValue(orderPackData,
						OrderPack[].class);
				List<OrderPack> orderPackList = Arrays.asList(orderPacks);
				orderPackList = Lists.transform(orderPackList, orderPack -> {
					orderPack.setIorderid(orderId);
					return orderPack;
				});

				savePackData = orderPackService
						.batchInsertOrderPack(orderPackList);
			} else {
				OrderPack orderPack = om.convertValue(orderPackData,
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
			log.info("save order pack is success, data : " + orderPackData);
			log.info("changeOrderStatusToDispatched: orderId is " + orderId
					+ ", result is " + changeOrdeStaus);
			return JSON.toJSONString(result);
		} else if (savePackData) {
			Map<String, Boolean> result = new HashMap<String, Boolean>();
			result.put("result", savePackData);
			log.info("save order pack is success, data : " + orderPackData);
			return JSON.toJSONString(result);
		} else {
			Map<String, Boolean> result = new HashMap<String, Boolean>();
			result.put("result", savePackData);
			log.info("save order pack is fair, data : " + orderPackData);
			throw new Exception(JSON.toJSONString(result));
		}
	}
}
