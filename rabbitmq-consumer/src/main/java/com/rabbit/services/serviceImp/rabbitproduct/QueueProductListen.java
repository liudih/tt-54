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
import com.rabbit.services.serviceImp.rabbitproduct.api.ProductApi;

public class QueueProductListen implements MessageListener {
	Logger log = Logger.getLogger(QueueProductListen.class.getName());

	private static final int defaultThreadSize = 2;
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

	private ExecutorService executor;
	@Autowired
	RabbitProductAdapt rabbitProductAdapt;
	@Autowired
	MonitorService monitorService;
	@Autowired
	ProductApi productApi;

	public QueueProductListen(int nThreads, int MAX_QUEUQ_SIZE) {
		this.nThreads = nThreads;
		this.MAX_QUEUQ_SIZE = MAX_QUEUQ_SIZE;
		executor = new ThreadPoolExecutor((nThreads == 0 ? defaultThreadSize
				: nThreads), (nThreads == 0 ? defaultThreadSize : nThreads),
				0L, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<Runnable>(
						(MAX_QUEUQ_SIZE == 0 ? defaultQueueSize
								: MAX_QUEUQ_SIZE)),
				new ThreadPoolExecutor.CallerRunsPolicy());
	}

	public void onMessage(final Message message) {// 此监听入口，如果数据操作发生异常，需要一层一层往外层抛，在消息入口统一处理
		// 监听服务，统一做成异步 +队列+批量处理方式，记录流水

		// 耗时且复杂的消息处理逻辑
		
		String key = "_";
		String receiveJson="";
		try {
			final String json = new String(message.getBody(), "UTF-8");
			receiveJson=json;
			// Map<String,Object> map=null;
			if (StringUtils.isEmpty(json)) {
				log.debug("QueueProductListen onMessage json empty!");
				return;
			}
			final Map<String, Object> map = (Map<String, Object>) JSON
					.parseObject(json);
			
			Iterator<String> iterator = map.keySet().iterator();
			key = iterator.next();
			final String opt = key;
//			executor.execute(new Runnable() {
//				@Override
//				public void run() {
					String state = "0";
					String failReason = "";
					try {
						if (MapUtils.isNotEmpty(map)) {
							log.error("onMessage---------------------->");
							messageAdapt(map, opt);
							state = "1";
//							log.info("onMessage success :" + json);
						} else {
							log.info("QueueProduct  onMessage map empty!");
						}
					} catch (UnsupportedEncodingException e1) {
						failReason = e1.getMessage();
						log.info("QueueProduct  onMessage error UnsupportedEncodingException!",
								e1);
					} catch (Exception e) {
						failReason = e.getMessage();
						log.error("QueueProductListen onMessage error!", e);
					}
					addMonitorRecord(opt, state, json, failReason);
//				}
//			});
		} catch (UnsupportedEncodingException e2) {
			log.error("onMessage getBody error!",e2);
			addMonitorRecord("_", "0", receiveJson, e2.getMessage());
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
			if (StringUtils.isNotEmpty(failReason)) {
				failReason = failReason.length() > 5000 ? failReason.substring(0,
						5000) : failReason;
			}
			rabbitMonitorDto.setNodeData(nodeData);
			rabbitMonitorDto.setFailReason(failReason);
			monitorService.addMonitorRecord(rabbitMonitorDto);
		}catch(Exception e){
			log.error("QueueProductListen addMonitorRecord error:",e);
		}
	}

	private void messageAdapt(Map<String, Object> map, String key)
			throws Exception {
		log.debug(Thread.currentThread().getName()+"  messageAdapt----------------->map:"+map);
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
		} else if (RabbitReceivedDataType.RABBIT_PRODUCT_UPDAET_QTY_TYPE
				.getKey().equals(key)) {
			JsonNode jsonNode = om.readTree((String) map.get(key));
			productApi.updatePorductQty(jsonNode);
		} else if (RabbitReceivedDataType.RABBIT_PRODUCT_SALE_PRICE_TYPE
				.getKey().equals(key)) {
			JsonNode jsonNode = om.readTree((String) map.get(key));
			productApi.addProductSalePrice(jsonNode);
		} else if (RabbitReceivedDataType.RABBIT_PRODUCT_PUSHMUTIL_TYPE
				.getKey().equals(key)) {
			
			String jsonString = JSON.toJSONString(map.get(key));
			JsonNode jsonNode = om.readTree(jsonString);
			productApi.pushMutil(jsonNode);
		} else if (RabbitReceivedDataType.RABBIT_PRODUCT_UPDAET_STORAGE_TYPE
				.getKey().equals(key)) {
			JsonNode jsonNode = om.readTree((String) map.get(key));
			productApi.updateProductStorage(jsonNode);
		} else if (RabbitReceivedDataType.RABBIT_PRODUCT_PUSH_PRODUCT_CATEGORY_TYPE
				.getKey().equals(key)) {
			JsonNode jsonNode = om.readTree((String) map.get(key));
			productApi.pushProductCategory(jsonNode);
		} else if (RabbitReceivedDataType.RABBIT_PRODUCT_UPDATE_STATUS_TYPE
				.getKey().equals(key)) {
			JsonNode jsonNode = om.readTree((String) map.get(key));
			productApi.updateProductStatus(jsonNode);
		} else if (RabbitReceivedDataType.RABBIT_PRODUCT_UPDATE_PRICE_TYPE
				.getKey().equals(key)) {
			JsonNode jsonNode = om.readTree((String) map.get(key));
			productApi.updateProductPrice(jsonNode);
		} else if (RabbitReceivedDataType.RABBIT_PRODUCT_ADD_PRODUCT_IMAGE_TYPE
				.getKey().equals(key)) {
			JsonNode jsonNode = om.readTree((String) map.get(key));
			productApi.saveProductImage(jsonNode);
		} else if (RabbitReceivedDataType.RABBIT_PRODUCT_UPDAET_TRANSLATION_TYPE
				.getKey().equals(key)) {
			JsonNode jsonNode = om.readTree((String) map.get(key));
			productApi.updateTranslation(jsonNode);
		} else if (RabbitReceivedDataType.RABBIT_PRODUCT_ADD_URL_TYPE.getKey()
				.equals(key)) {
			JsonNode jsonNode = om.readTree((String) map.get(key));
			productApi.addProductUrl(jsonNode);
		} else if (RabbitReceivedDataType.RABBIT_PRODUCT_ADD_SELLING_POINT_TYPE
				.getKey().equals(key)) {
			JsonNode jsonNode = om.readTree((String) map.get(key));
			productApi.addProductSellingPoints(jsonNode);
		} else if (RabbitReceivedDataType.RABBIT_PRODUCT_ADD_LABEL_TYPE
				.getKey().equals(key)) {
			JsonNode jsonNode = om.readTree((String) map.get(key));
			productApi.addProductLabelType(jsonNode);
		} else if (RabbitReceivedDataType.RABBIT_PRODUCT_DELETE_LABEL_TYPE
				.getKey().equals(key)) {
			JsonNode jsonNode = om.readTree((String) map.get(key));
			productApi.deleteProductLabel(jsonNode);
		} else if (RabbitReceivedDataType.RABBIT_PRODUCT_UPDAET_LABEL_TYPE
				.getKey().equals(key)) {
			JsonNode jsonNode = om.readTree((String) map.get(key));
			productApi.changeProductLabel(jsonNode);
		} else if (RabbitReceivedDataType.RABBIT_PRODUCT_ADD_TRANSLATION_TYPE
				.getKey().equals(key)) {
			JsonNode jsonNode = om.readTree((String) map.get(key));
			productApi.AddTranslation(jsonNode);
		} else if (RabbitReceivedDataType.RABBIT_PRODUCT_UPDAET_FREIGHT_TYPE
				.getKey().equals(key)) {
			JsonNode jsonNode = om.readTree((String) map.get(key));
			productApi.updateProductFreight(jsonNode);
		} else if (RabbitReceivedDataType.RABBIT_PRODUCT_DELETE_CURRENT_SALEPRICE_TYPE
				.getKey().equals(key)) {
			String str = (String) map.get(key);
			productApi.deleteProductCurrentSalePrice(str);
		} else if (RabbitReceivedDataType.RABBIT_PRODUCT_DELETE_SELLING_POING_TYPE
				.getKey().equals(key)) {
			JsonNode jsonNode = om.readTree((String) map.get(key));
			List<String> params = RabbitReceivedDataType.RABBIT_PRODUCT_DELETE_SELLING_POING_TYPE
					.getParams();
			Integer languageId = om.convertValue(jsonNode.get(params.get(1)),
					Integer.class);
			String convertValue = om.convertValue(jsonNode.get(params.get(0)),
					String.class);
			productApi.deleteProductSellingPoints(convertValue, languageId);
		} else if (RabbitReceivedDataType.RABBIT_PRODUCT_DELETE_SALE_PRICE_TYPE
				.getKey().equals(key)) {
			JsonNode jsonNode = om.readTree((String) map.get(key));
			productApi.deleteProductSalePrice(jsonNode);
		} else if (RabbitReceivedDataType.RABBIT_PRODUCT_COTY_TYPE.getKey()
				.equals(key)) {
			JsonNode jsonNode = om.readTree((String) map.get(key));
			productApi.copy(jsonNode);
		} else if (RabbitReceivedDataType.RABBIT_PRODUCT_DELETE_IMAGE_TYPE
				.getKey().equals(key)) {
			String josn=JSON.toJSONString(map.get(key));
			JsonNode jsonNode = om.readTree(josn);
			List<String> params = RabbitReceivedDataType.RABBIT_PRODUCT_DELETE_IMAGE_TYPE
					.getParams();
			Integer websiteId = om.convertValue(jsonNode.get(params.get(1)),
					Integer.class);
			String listingId = om.convertValue(jsonNode.get(params.get(0)),
					String.class);
			productApi.deleteProductImage(listingId, websiteId);
		} else if (RabbitReceivedDataType.RABBIT_PRODUCT_ADD_CATEGORY_TYPE
				.getKey().equals(key)) {
			JsonNode jsonNode = om.readTree((String) map.get(key));
			productApi.addProductCategory(jsonNode);
		} else {
			log.info("QueueProduct messageAdapt no match adapt key! map:" + map);
		}
	}

}
