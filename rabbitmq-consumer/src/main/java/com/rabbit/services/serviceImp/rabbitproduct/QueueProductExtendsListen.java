package com.rabbit.services.serviceImp.rabbitproduct;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbit.common.enums.rabbitmq.RabbitReceivedDataType;
import com.rabbit.common.util.DateFormatUtils;
import com.rabbit.dto.monitor.RabbitMonitorDto;
import com.rabbit.services.serviceImp.monitor.MonitorService;
import com.rabbit.services.serviceImp.rabbitproduct.api.AttributeApi;
import com.rabbit.services.serviceImp.rabbitproduct.api.CategoryApi;
import com.rabbit.services.serviceImp.rabbitproduct.api.ThirdPlatformDataApi;

public class QueueProductExtendsListen implements MessageListener {
	Logger log = Logger.getLogger(QueueProductExtendsListen.class.getName());

	private static final int defaultThreadSize = 1;
	private static final int defaultQueueSize = 1000;
	private int nThreads;

	public int getnThreads() {
		return nThreads;
	}

	public void setnThreads(int nThreads) {
		this.nThreads = nThreads;
	}

	private int MAX_QUEUQ_SIZE;

	public int getMAX_QUEUQ_SIZE() {
		return MAX_QUEUQ_SIZE;
	}

	public void setMAX_QUEUQ_SIZE(int mAX_QUEUQ_SIZE) {
		MAX_QUEUQ_SIZE = mAX_QUEUQ_SIZE;
	}

	@Autowired
	RabbitProductAdapt rabbitProductAdapt;

	@Autowired
	ThirdPlatformDataApi thirdPlatformDataApi;
	@Autowired
	AttributeApi attributeApi;
	@Autowired
	MonitorService monitorService;
	@Autowired
	CategoryApi categoryApi;
	private ExecutorService executor;

	public QueueProductExtendsListen(int nThreads, int MAX_QUEUQ_SIZE) {
		this.nThreads = nThreads;
		this.MAX_QUEUQ_SIZE = MAX_QUEUQ_SIZE;
		executor = new ThreadPoolExecutor((nThreads == 0 ? defaultThreadSize
				: nThreads), (nThreads == 0 ? defaultThreadSize : nThreads),
				0L, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<Runnable>(
						(MAX_QUEUQ_SIZE == 0 ? defaultQueueSize
								: MAX_QUEUQ_SIZE)),
				new ThreadPoolExecutor.CallerRunsPolicy());
	}

	public void onMessage(Message message) {

		String key = "_";
		String receiveJson="";
		try {
			String jsonParam = new String(message.getBody(), "UTF-8");
			receiveJson=jsonParam;
			// Map<String,Object> map=null;
			if (StringUtils.isEmpty(jsonParam)) {
				log.debug("QueueProductExtendsListen onMessage json empty!");
				return;
			}
			final Map<String, Object> map = (Map<String, Object>) JSON
					.parseObject(jsonParam);
			final String json=jsonParam;
			Iterator<String> iterator = map.keySet().iterator();
			key = iterator.next();
			final String opt=key;
			executor.execute(new Runnable() {
				@Override
				public void run() {
					String failReason = "";
					String state = "0";
					try {
						
						if (MapUtils.isNotEmpty(map)) {
							messageAdapt(map, opt);
							state = "1";
							log.info("onMessage success :" + json);
						} else {
							log.info("QueueProductExtendsListen  onMessage map empty!");
						}
					} catch (UnsupportedEncodingException e1) {
						failReason = e1.getMessage();
						log.info("QueueProductExtendsListen  onMessage error UnsupportedEncodingException!",
								e1);
					} catch (Exception e) {
						failReason = e.getMessage();
						log.error("QueueProductExtendsListen onMessage error!",
								e);
					}
					addMonitorRecord(opt, state, json, failReason);
				}
			});
		} catch (UnsupportedEncodingException e2) {
			log.debug("QueueProductListen onMessage json empty!");
			addMonitorRecord("_", "0", receiveJson, e2.getMessage());
		}

	}

	private void addMonitorRecord(String optType, String state,
			String nodeData, String failReason) {
		try{
			if(!RabbitReceivedDataType.RABBIT_THIREDPLATFORMDATE_PUSH_TYPE//过滤掉第三方数据
					.getKey().equals(optType)){
				
				RabbitMonitorDto rabbitMonitorDto = new RabbitMonitorDto();
				String date=DateFormatUtils.getUtcDateStr(new Date());
				rabbitMonitorDto.setRecordState(state);
				rabbitMonitorDto.setRecordKey(optType);
				rabbitMonitorDto.setCreatedOn(date);
				rabbitMonitorDto.setLastUpdatedBy("api");
				rabbitMonitorDto.setLastUpdatedOn(date);
				rabbitMonitorDto.setCreatedBy("api");
				rabbitMonitorDto.setIsDeleted("0");
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
				monitorService.addMonitorRecord(rabbitMonitorDto);
			}
			
		}catch(Exception e){
			log.error("QueueProductExtendsListen addMonitorRecord error:",e);
		}
	}

	private void messageAdapt(Map<String, Object> map, String key)
			throws Exception {

		ObjectMapper om = new ObjectMapper();
		if (RabbitReceivedDataType.RABBIT_PRODUCT_PUSH_TYPE.getKey()
				.equals(key)) {
			@SuppressWarnings("unchecked")
			// 因为此方法有多个参数，所以需要重新封装一层参数结构
			Map<String, Object> paramsMap = (Map<String, Object>) map.get(key);
			List<String> params = RabbitReceivedDataType.RABBIT_PRODUCT_PUSH_TYPE
					.getParams();
			String jsonString = JSON.toJSONString(paramsMap.get(params.get(0)));
			JsonNode jsonNode = om.readTree(jsonString);
			rabbitProductAdapt.push(jsonNode,
					(String) paramsMap.get(params.get(1)));
		} else if (RabbitReceivedDataType.RABBIT_ATTRIBUTE_ADD_PRODUCT_ATTRIBUTE_TYPE
				.getKey().equals(key)) {
			JsonNode jsonNode = om.readTree((String) map.get(key));
			attributeApi.addProductAttribute(jsonNode);
		} else if (RabbitReceivedDataType.RABBIT_ATTRIBUTE_ADD_PRODUCT_MULTI_ATTRIBUTE_TYPE
				.getKey().equals(key)) {
			JsonNode jsonNode = om.readTree((String) map.get(key));
			attributeApi.addProductMultiAttribute(jsonNode);
		} else if (RabbitReceivedDataType.RABBIT_THIREDPLATFORMDATE_PUSH_TYPE
				.getKey().equals(key)) {
			JsonNode jsonNode = om.readTree((String) map.get(key));
			thirdPlatformDataApi.push(jsonNode);
//		} else if (RabbitReceivedDataType.RABBIT_KEYWORD_PUSH_TYPE.getKey()
//				.equals(key)) {
//			JsonNode jsonNode = om.readTree((String) map.get(key));
//			keywordApi.push(jsonNode);
		} else if (RabbitReceivedDataType.RABBIT_ATTRIBUTE_PUSH_TYPE.getKey()
				.equals(key)) {
			JsonNode jsonNode = om.readTree((String) map.get(key));
			attributeApi.push(jsonNode);
		} else if (RabbitReceivedDataType.RABBIT_ATTRIBUTE_DELETE_MULTI_PRODUCT_ATTRIBUTE_TYPE
				.getKey().equals(key)) {
			@SuppressWarnings("unchecked")
			Map<String, Object> mapParams = (Map<String, Object>) map.get(key);
			List<String> params = RabbitReceivedDataType.RABBIT_ATTRIBUTE_DELETE_MULTI_PRODUCT_ATTRIBUTE_TYPE
					.getParams();
			String parentSku = (String) mapParams.get(params.get(0));
			String keyParams = (String) mapParams.get(params.get(1));
			Integer languageId = (Integer) mapParams.get(params.get(2));
			Integer websiteId = (Integer) mapParams.get(params.get(3));
			attributeApi.deleteMultiProductAttribute(parentSku, keyParams,
					languageId, websiteId);
		} else if (RabbitReceivedDataType.RABBIT_ATTRIBUTE_DELETE_PRODUCT_ATTRIBUTE_TYPE
				.getKey().equals(key)) {
			@SuppressWarnings("unchecked")
			Map<String, Object> mapParams = (Map<String, Object>) map.get(key);
			List<String> params = RabbitReceivedDataType.RABBIT_ATTRIBUTE_DELETE_PRODUCT_ATTRIBUTE_TYPE
					.getParams();
			String listingid = (String) mapParams.get(params.get(0));
			String keyParams = (String) mapParams.get(params.get(1));
			Integer languageId = (Integer) mapParams.get(params.get(2));
			attributeApi.deleteProductAttribute(listingid, keyParams,
					languageId);
		} else if (RabbitReceivedDataType.RABBIT_CATEGORY_PUSH_TYPE.getKey()
				.equals(key)) {
			JsonNode jsonNode = om.readTree((String) map.get(key));
			categoryApi.push(jsonNode);
		} else if (RabbitReceivedDataType.RABBIT_CATEGORY_SAVE_UPDATE_CATEGORY_BASE_TYPE
				.getKey().equals(key)) {
			JsonNode jsonNode = om.readTree((String) map.get(key));
			categoryApi.saveOrUpdateCategoryBase(jsonNode);
		} else if (RabbitReceivedDataType.RABBIT_CATEGORY_PLATFORM_PUSH_TYPE
				.getKey().equals(key)) {
			JsonNode jsonNode = om.readTree((String) map.get(key));
			categoryApi.saveOrUpdateCategoryBase(jsonNode);
		}/*
		 * else if(RabbitReceivedDataType.
		 * RABBIT_CATEGORY_SAVE_UPDATE_PLATFORM_CATEGORY_TYPE.getKey().equals(
		 * key)){ String str = JSON.toJSONString(map.get(key)); JsonNode
		 * jsonNode=om.readTree(str);
		 * categoryApi.saveOrUpdatePlatformCategory(jsonNode); }
		 */else if (RabbitReceivedDataType.RABBIT_CATEGORY_GET_TYPE.getKey()
				.equals(key)) {
			@SuppressWarnings("unchecked")
			Map<String, Object> paramsMap = (Map<String, Object>) map.get(key);
			List<String> params = RabbitReceivedDataType.RABBIT_CATEGORY_GET_TYPE
					.getParams();
			Integer websiteid = (Integer) paramsMap.get(params.get(0));
			Integer languageid = (Integer) paramsMap.get(params.get(1));
			categoryApi.get(websiteid, languageid);
		} else if (RabbitReceivedDataType.RABBIT_CATEGORY_SAVE_CATEGORY_ATTRIBUTES_TYPE
				.getKey().equals(key)) {
			JsonNode jsonNode = om.readTree((String) map.get(key));
			categoryApi.saveCategoryAttributes(jsonNode);
		} else {
			log.info("QueueProductExtendsListen messageAdapt no match adapt key! map:"
					+ map);
		}
	}

}
