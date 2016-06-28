package com.rabbit.controller;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.rabbit.entry.RabbitMonitorDto;
import com.rabbit.entry.rabbit.config.OrderMqConfig;
import com.rabbit.enums.RabbitReceivedDataType;
import com.rabbit.services.MonitorService;
import com.rabbit.services.SendService;

@Controller
public class OrderContoller {
	private static Logger log = Logger.getLogger(OrderContoller.class.getName());
	@Autowired
	SendService sendService;
	@Autowired
	OrderMqConfig orderConfig;
	
	@Autowired
	MonitorService monitorService;
	private void addMonitorRecord(String optType, String state,
			String nodeData, String failReason) {
		try{
			
			RabbitMonitorDto rabbitMonitorDto = new RabbitMonitorDto();
			rabbitMonitorDto.setRecordState(state);
			rabbitMonitorDto.setRecordKey(optType);
			rabbitMonitorDto.setOptType(optType);
			rabbitMonitorDto.setNodeData(nodeData);
			rabbitMonitorDto.setFailReason(failReason);
			monitorService.addMonitorRecord(rabbitMonitorDto);
		}catch(Exception e){
			log.error("Attribute addMonitorRecord error:",e);
		}
	}
	/**
	 * 修改订单状态为Dispatched
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/checkout/changeOrderStatusToDispatched", method = { RequestMethod.POST })
	@ResponseBody
	public String changeOrderStatusToDispatched(@RequestBody String jsonParam) {
		addMonitorRecord( RabbitReceivedDataType.RABBIT_ORDER_CHANGE_ORDER_STATUS_TO_DISPATCHED.getKey(),
				"1", jsonParam,  null);
		Map<String, Boolean> result = new HashMap<String, Boolean>();
		try{
			Map<String, Object> jsonMap = new HashMap<String, Object>();
			jsonMap.put(RabbitReceivedDataType.RABBIT_ORDER_CHANGE_ORDER_STATUS_TO_DISPATCHED.getKey(), jsonParam);
			
			sendService.sendMessage(jsonMap, orderConfig.getRouteKey(),
					orderConfig.getAmqpTemplate());
			result.put("result", true);
		} catch (Exception e) {
			log.error("changeOrderStatusToDispatched   error:",e);
			addMonitorRecord( RabbitReceivedDataType.RABBIT_ORDER_CHANGE_ORDER_STATUS_TO_DISPATCHED.getKey(),
					"0", jsonParam,  e.getMessage());
			throw e;
		}
		return JSON.toJSONString(result);
	}
	
	
	/**
	 * 修改订单状态为COMPLETED
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/checkout/changeOrderStatusToCompleted", method = { RequestMethod.POST })
	@ResponseBody
	public String changeOrderStatusToCompleted(@RequestBody String jsonParam) {

		addMonitorRecord( RabbitReceivedDataType.RABBIT_ORDER_CHANGE_ORDER_STATUS_TO_COMPLETED.getKey(),
				"1", jsonParam,  null);
		Map<String, Boolean> result = new HashMap<String, Boolean>();
		try{
			Map<String, Object> jsonMap = new HashMap<String, Object>();
			jsonMap.put(RabbitReceivedDataType.RABBIT_ORDER_CHANGE_ORDER_STATUS_TO_COMPLETED.getKey(), jsonParam);
			
			sendService.sendMessage(jsonMap, orderConfig.getRouteKey(),
					orderConfig.getAmqpTemplate());
			result.put("result", true);
		} catch (Exception e) {
			log.error("changeOrderStatusToCompleted   error:",e);
			addMonitorRecord( RabbitReceivedDataType.RABBIT_ORDER_CHANGE_ORDER_STATUS_TO_COMPLETED.getKey(),
					"0", jsonParam,  e.getMessage());
			throw e;
		}
		return JSON.toJSONString(result);
	}
	
	/**
	 * 修改订单状态为ONHOLD
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/checkout/changeOrderStatusToOnHold", method = { RequestMethod.POST })
	@ResponseBody
	public String changeOrderStatusToOnHold(@RequestBody String jsonParam) {
		
		addMonitorRecord( RabbitReceivedDataType.RABBIT_ORDER_CHANGE_ORDER_STATUS_TO_ONHOLD.getKey(),
				"1", jsonParam,  null);
		Map<String, Boolean> result = new HashMap<String, Boolean>();
		try{
			Map<String, Object> jsonMap = new HashMap<String, Object>();
			jsonMap.put(RabbitReceivedDataType.RABBIT_ORDER_CHANGE_ORDER_STATUS_TO_ONHOLD.getKey(), jsonParam);
			
			sendService.sendMessage(jsonMap, orderConfig.getRouteKey(),
					orderConfig.getAmqpTemplate());
			result.put("result", true);
		} catch (Exception e) {
			log.error("changeOrderStatusToOnHold   error:",e);
			addMonitorRecord( RabbitReceivedDataType.RABBIT_ORDER_CHANGE_ORDER_STATUS_TO_ONHOLD.getKey(),
					"0", jsonParam,  e.getMessage());
			throw e;
		}
		return JSON.toJSONString(result);
	}
	
	/**
	 * 修改订单状态为Processing
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/checkout/changeOrderStatusToProcessing", method = { RequestMethod.POST })
	@ResponseBody
	public String changeOrderStatusToProcessing(@RequestBody String jsonParam) {
		
		addMonitorRecord( RabbitReceivedDataType.RABBIT_ORDER_CHANGE_ORDER_STATUS_TO_PROCESSING.getKey(),
				"1", jsonParam,  null);
		Map<String, Boolean> result = new HashMap<String, Boolean>();
		try{
			Map<String, Object> jsonMap = new HashMap<String, Object>();
			jsonMap.put(RabbitReceivedDataType.RABBIT_ORDER_CHANGE_ORDER_STATUS_TO_PROCESSING.getKey(), jsonParam);
			
			sendService.sendMessage(jsonMap, orderConfig.getRouteKey(),
					orderConfig.getAmqpTemplate());
			result.put("result", true);
		} catch (Exception e) {
			log.error("changeOrderStatusToProcessing   error:",e);
			addMonitorRecord( RabbitReceivedDataType.RABBIT_ORDER_CHANGE_ORDER_STATUS_TO_PROCESSING.getKey(),
					"0", jsonParam,  e.getMessage());
			throw e;
		}
		return JSON.toJSONString(result);
	}
	
	/**
	 * 修改订单状态为Refunded
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/checkout/changeOrderStatusToRefunded", method = { RequestMethod.POST })
	@ResponseBody
	public String changeOrderStatusToRefunded(@RequestBody String jsonParam) {
		
		addMonitorRecord( RabbitReceivedDataType.RABBIT_ORDER_CHANGE_ORDER_STATUS_TO_REFUNDED.getKey(),
				"1", jsonParam,  null);
		Map<String, Boolean> result = new HashMap<String, Boolean>();
		try{
			Map<String, Object> jsonMap = new HashMap<String, Object>();
			jsonMap.put(RabbitReceivedDataType.RABBIT_ORDER_CHANGE_ORDER_STATUS_TO_REFUNDED.getKey(), jsonParam);
			
			sendService.sendMessage(jsonMap, orderConfig.getRouteKey(),
					orderConfig.getAmqpTemplate());
			result.put("result", true);
		} catch (Exception e) {
			log.error("changeOrderStatusToRefunded   error:",e);
			addMonitorRecord( RabbitReceivedDataType.RABBIT_ORDER_CHANGE_ORDER_STATUS_TO_REFUNDED.getKey(),
					"0", jsonParam,  e.getMessage());
			throw e;
		}
		return JSON.toJSONString(result);
	}
}
