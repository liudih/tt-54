package  com.rabbit.controller;


import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.rabbit.entry.RabbitMonitorDto;
import com.rabbit.entry.rabbit.config.ProductMqExtendsConfig;
import com.rabbit.enums.RabbitReceivedDataType;
import com.rabbit.services.MonitorService;
import com.rabbit.services.SendService;

@Controller
public class Attribute{

	private static Logger log=Logger.getLogger(Attribute.class.getName());
	@Autowired
	SendService sendService;
	@Autowired
	ProductMqExtendsConfig productMqExtendsConfig;

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
	@RequestMapping(value = "/api/attribute", method=RequestMethod.GET)
	@ResponseBody
	public String getAll(Integer languageid) {
		/*return ok(Json.toJson(attributeEnquityDao.getAll(languageid)));*/
		return "";
	}
	private void getProductExtends(String jsonParam ,String key) throws Exception{
		ObjectMapper om = new ObjectMapper();
		JsonNode jnode =om.readTree(jsonParam);
		if (null == jnode && StringUtils.isEmpty(key)) {
			log.info("Expecting Json data");
			throw new Exception("Expecting Json data");
		}
		Map<String,Object> map=new HashMap<String,Object>();
		map.put(key, jsonParam);
		sendService.sendMessage(map, productMqExtendsConfig.getRouteKey(), productMqExtendsConfig.getAmqpTemplate());
	}
	
	/*@ApiHistory(type = HandleReceivedDataType.ADD_ATTRIBUTE, createuser = "api")*/
	@RequestMapping(value = "/api/attribute/push", method=RequestMethod.POST)
	@ResponseBody
	public String push(@RequestBody String jsonParam) throws Exception {
		addMonitorRecord( RabbitReceivedDataType.RABBIT_ATTRIBUTE_PUSH_TYPE.getKey(),
				"1", jsonParam,  null);
		try {
			getProductExtends(jsonParam,RabbitReceivedDataType.RABBIT_ATTRIBUTE_PUSH_TYPE.getKey());
			return "successfully";
		} catch (Exception p) {
			log.error("Attribute push error",p);
			addMonitorRecord( RabbitReceivedDataType.RABBIT_ATTRIBUTE_PUSH_TYPE.getKey(),
					"0", jsonParam,  p.getMessage());
			throw p;
		}
	}

	/**
	 * add product mutil attr
	 * {"sku":"","websiteId":1,"multiAttributes":[{"keyId":null,"key":
	 * "","valueId": null,"value":"","languangeId":1,"showImg":"false"}]}
	 * 
	 * @return
	 * @throws Exception 
	 */
	
	/*@ApiHistory(type = HandleReceivedDataType.ADD_PRODUCT_MULTI_ATTRIBUTE, createuser = "api")*/
	@RequestMapping(value = "/api/mutilproduct/attribute", method=RequestMethod.POST)
	@ResponseBody
	public String addProductMultiAttribute(@RequestBody String jsonParam) throws Exception {
		addMonitorRecord( RabbitReceivedDataType.RABBIT_ATTRIBUTE_ADD_PRODUCT_MULTI_ATTRIBUTE_TYPE.getKey(),
				"1", jsonParam,  null);
		try{
			//long currentTimeMillis = System.currentTimeMillis();//获取当前时间
			//for(int i=0;i<20000;i++){
				getProductExtends(jsonParam,RabbitReceivedDataType.RABBIT_ATTRIBUTE_ADD_PRODUCT_MULTI_ATTRIBUTE_TYPE.getKey());
			//}
			//long endTime = System.currentTimeMillis();
			//System.out.println("程序运行时间3："+(endTime-currentTimeMillis)+"ms");
			return "successfully";
		} catch (Exception p) {
			log.error("Attribute addProductMultiAttribute error",p);
			addMonitorRecord( RabbitReceivedDataType.RABBIT_ATTRIBUTE_ADD_PRODUCT_MULTI_ATTRIBUTE_TYPE.getKey(),
					"0", jsonParam,  p.getMessage());
			throw p;
		}
	}

	/**
	 * {"parentSku":"","key":"","languageId":1,"websiteId":1}
	 * 
	 * @return
	 * @throws Exception 
	 */
	/*@ApiHistory(type = HandleReceivedDataType.DELETE_PRODUCT_MULTI_ATTRIBUTE, createuser = "api")*/
	@RequestMapping(value = "/api/mutilproduct/attribute/{parentSku}/{key}/{languageId}/{websiteId}", method=RequestMethod.GET)
	@ResponseBody
	public String deleteMultiProductAttribute(@PathVariable("parentSku") String parentSku,
			@PathVariable("key") String key,
			@PathVariable("languageId") Integer languageId,
			@PathVariable("websiteId") Integer websiteId
			) throws Exception {
		Map<String,Object> paramMap=new HashMap<String,Object>();
		try {
			paramMap.put("parentSku", parentSku);
			paramMap.put("key", key);
			paramMap.put("languageId", languageId);
			paramMap.put("websiteId", websiteId);
			
			addMonitorRecord( RabbitReceivedDataType.RABBIT_ATTRIBUTE_DELETE_MULTI_PRODUCT_ATTRIBUTE_TYPE.getKey(),
					"1", JSON.toJSONString(paramMap),  null);
			Map<String,Object> map=new HashMap<String,Object>();
			map.put(RabbitReceivedDataType.RABBIT_ATTRIBUTE_DELETE_MULTI_PRODUCT_ATTRIBUTE_TYPE.getKey(), paramMap);
			sendService.sendMessage(map, productMqExtendsConfig.getRouteKey(), productMqExtendsConfig.getAmqpTemplate());
			
			return "success";
		} catch (Exception p) {
			log.error("attibute deleteMultiProductAttribute error!",p);
			addMonitorRecord( RabbitReceivedDataType.RABBIT_ATTRIBUTE_DELETE_MULTI_PRODUCT_ATTRIBUTE_TYPE.getKey(),
					"0", JSON.toJSONString(paramMap),  p.getMessage());
			throw p;
			
		}
	}

	/**
	 * {"listingId":"","key":"","languageId":""}
	 * 
	 * @return
	 * @throws Exception 
	 */
	/*@ApiHistory(type = HandleReceivedDataType.DELETE_PRODUCT_ATTRIBUTE, createuser = "api")*/
	@RequestMapping(value = "/api/product/attribute/{listingid}/{key}/{languageId}", method=RequestMethod.DELETE)
	@ResponseBody
	public String deleteProductAttribute(@PathVariable("listingid") String listingid,
			@PathVariable("key") String key,
			@PathVariable("languageId") Integer languageId
			) throws Exception {
		Map<String,Object> paramMap=new HashMap<String,Object>();
		try {
			paramMap.put("listingid", listingid);
			paramMap.put("key", key);
			paramMap.put("languageId", languageId);
			addMonitorRecord( RabbitReceivedDataType.RABBIT_ATTRIBUTE_DELETE_PRODUCT_ATTRIBUTE_TYPE.getKey(),
					"1", JSON.toJSONString(paramMap),  null);
			
			Map<String,Object> map=new HashMap<String,Object>();
			map.put(RabbitReceivedDataType.RABBIT_ATTRIBUTE_DELETE_PRODUCT_ATTRIBUTE_TYPE.getKey(), paramMap);
			sendService.sendMessage(map, productMqExtendsConfig.getRouteKey(), productMqExtendsConfig.getAmqpTemplate());
			
			return "success";
		} catch (Exception p) {
			log.error("attribute deleteProductAttribute error!",p);
			addMonitorRecord( RabbitReceivedDataType.RABBIT_ATTRIBUTE_DELETE_PRODUCT_ATTRIBUTE_TYPE.getKey(),
					"0", JSON.toJSONString(paramMap),  p.getMessage());
			throw p;
		}
	}

	/**
	 * {"listingId":"","sku":"","websiteId":1,"attributes":[{"keyId":null,"key":
	 * "","valueId": null,"value":"","languangeId":1,"showImg":"false"}]}
	 * 
	 * @return
	 * @throws Exception 
	 */
	/*@ApiHistory(type = HandleReceivedDataType.ADD_PRODUCT_ATTRIBUTE, createuser = "api")*/
	@RequestMapping(value = "/api/product/attribute", method=RequestMethod.POST)
	@ResponseBody
	public String addProductAttribute(@RequestBody String jsonParam) throws Exception {
		try {
			addMonitorRecord( RabbitReceivedDataType.RABBIT_ATTRIBUTE_ADD_PRODUCT_ATTRIBUTE_TYPE.getKey(),
					"1", jsonParam,  null);
			
			getProductExtends(jsonParam,RabbitReceivedDataType.RABBIT_ATTRIBUTE_ADD_PRODUCT_ATTRIBUTE_TYPE.getKey());
			return "successfully";
		} catch (Exception p) {
			log.error("Attribute addProductAttribute error!",p);
			addMonitorRecord( RabbitReceivedDataType.RABBIT_ATTRIBUTE_ADD_PRODUCT_ATTRIBUTE_TYPE.getKey(),
					"0",jsonParam,  p.getMessage());
			throw p;
		}
	}

}
