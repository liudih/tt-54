package com.rabbit.services.serviceImp.rabbitproduct;

import java.util.Date;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbit.common.enums.RabbitReceivedDataType;
import com.rabbit.common.util.DateFormatUtils;
import com.rabbit.conf.mapper.monitor.MonitorRecordsMapper;
import com.rabbit.dto.monitor.RabbitMonitorDto;
import com.rabbit.services.QueueOneLitener;
import com.rabbit.services.iservice.shipping.IShippingMethodService;
import com.rabbit.services.serviceImp.rabbitproduct.api.OrderApi;


public class OrderQueueLitener implements MessageListener{
	Logger log=Logger.getLogger(QueueOneLitener.class.getName());
	@Autowired
	MonitorRecordsMapper monitorRecordsMapper;
	@Autowired
	OrderApi orderApi;
	@Autowired
	IShippingMethodService shippingMethodService;
	public void onMessage(Message message) {
		
		String json="";
		String key ="";
		try {
			json = new String(message.getBody(), "UTF-8");
			if (StringUtils.isEmpty(json)) {
				log.debug("QueueOneLitener onMessage json empty!");
				return;
			}
			final Map<String, Object> map = (Map<String, Object>) JSON
					.parseObject(json);
			Iterator<String> iterator = map.keySet().iterator();
			key = iterator.next();
				messageAdapt(map,key);
				addMonitorRecord(key,"1",json,null);
		} catch (Exception e) {
			log.error("QueueOneLitener error!  json:"+json,e);
			addMonitorRecord(key,"0",json,e.getMessage());
		}
    } 
	private void addMonitorRecord(String optType, String state,
			String nodeData, String failReason) {
		try{
			RabbitMonitorDto rabbitMonitorDto = new RabbitMonitorDto();
			String date=DateFormatUtils.getUtcDateStr(new Date());
			rabbitMonitorDto.setRecordState(state);
			rabbitMonitorDto.setRecordKey(optType);
			rabbitMonitorDto.setCreatedOn(DateFormatUtils.getUtcDateStr(new Date()));
			rabbitMonitorDto.setCreatedBy("api");
			rabbitMonitorDto.setIsDeleted("0");
			rabbitMonitorDto.setLastUpdatedBy("api");
			rabbitMonitorDto.setLastUpdatedOn(date);
			rabbitMonitorDto.setOptType(optType);
			if (StringUtils.isNotEmpty(nodeData)) {
				nodeData = nodeData.length() > 5000 ? nodeData.substring(0, 5000)
						: nodeData;
			}
			if (StringUtils.isNotEmpty(failReason)) {
				failReason = failReason.length() > 5000 ? failReason.substring(0,
						5000) : failReason;
			}
			rabbitMonitorDto.setNodeData(nodeData);
			rabbitMonitorDto.setFailReason(failReason);
			monitorRecordsMapper.addMonitorRecord(rabbitMonitorDto);
		}catch(Exception e){
			log.error("OrderQueueLitener addMonitorRecord error:",e);
		}
	}
	private void messageAdapt(Map<String,Object> map,String opt) throws Exception{
		if(MapUtils.isEmpty(map) || StringUtils.isEmpty(opt)){
			log.info("messageAdapt -------------------------------> param empty! map:"+map+"  opt:"+opt);
			return;
		}
		String result="";
		ObjectMapper om = new ObjectMapper();
		if(RabbitReceivedDataType.RABBIT_ORDER_CHANGE_ORDER_STATUS_TO_DISPATCHED.getKey().equals(opt)){
			//JsonNode jsonNode = om.readTree((String) map.get(opt));
			 result=orderApi.changeOrderStatusToDispatched((String) map.get(opt));
		}else if(RabbitReceivedDataType.RABBIT_ORDER_CHANGE_ORDER_STATUS_TO_COMPLETED.getKey().equals(opt)){
			JsonNode jsonNode = om.readTree((String) map.get(opt));
			 result=orderApi.changeOrderStatusToCompleted(jsonNode);
		}else if(RabbitReceivedDataType.RABBIT_ORDER_CHANGE_ORDER_STATUS_TO_ONHOLD.getKey().equals(opt)){
			JsonNode jsonNode = om.readTree((String) map.get(opt));
			 result=orderApi.changeOrderStatusToOnHold(jsonNode);
		}else if(RabbitReceivedDataType.RABBIT_ORDER_CHANGE_ORDER_STATUS_TO_PROCESSING.getKey().equals(opt)){
			JsonNode jsonNode = om.readTree((String) map.get(opt));
			 result=orderApi.changeOrderStatusToProcessing(jsonNode);
		}else if(RabbitReceivedDataType.RABBIT_ORDER_CHANGE_ORDER_STATUS_TO_REFUNDED.getKey().equals(opt)){
			JsonNode jsonNode = om.readTree((String) map.get(opt));
			 result=orderApi.changeOrderStatusToRefunded(jsonNode);
		}else if(RabbitReceivedDataType.RABBIT_SHIPPING_PUSH_TYPE.getKey().equals(opt)){
			String data=(String)map.get(opt);
			JsonNode jsonNode = om.readTree(data);
			 result=shippingMethodService.push(jsonNode);
		}else{
			log.info("QueueOneLitener messageAdapt not map key find");
		}
		log.info("QueueOneLitener messageAdapt result:"+result);
	}
	
}
