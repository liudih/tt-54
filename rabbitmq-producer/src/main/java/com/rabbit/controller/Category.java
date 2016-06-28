package com.rabbit.controller;

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

import com.rabbit.entry.rabbit.config.ProductMqExtendsConfig;
import com.rabbit.enums.RabbitReceivedDataType;
import com.rabbit.services.SendService;

/*@ApiPermission*/
@Controller
public class Category {

	private static Logger log=Logger.getLogger(Category.class.getName());
	@Autowired
	SendService sendService;
	@Autowired
	ProductMqExtendsConfig productMqExtendsConfig;
	private void getProductExtends(String jsonParam ,String key) throws Exception{
		ObjectMapper om = new ObjectMapper();
		JsonNode jnode =om.readTree(jsonParam);
		if (null == jnode && StringUtils.isEmpty(key)) {
			log.info("Expecting Json data");
			throw new Exception("Expecting Json data");
		}
		Map<String,Object> map=new HashMap<String,Object>();
		map.put(key, jnode);
		sendService.sendMessage(map, productMqExtendsConfig.getRouteKey(), productMqExtendsConfig.getAmqpTemplate());
	}
	@RequestMapping(value = "/api/category/push", method=RequestMethod.POST)
	@ResponseBody
	public String push(@RequestBody String jsonParam) throws Exception {
		try {
			getProductExtends(jsonParam,RabbitReceivedDataType.RABBIT_CATEGORY_PUSH_TYPE.getKey());
			return "successfully";
		} catch (Exception p) {
			log.error("Category push error!",p);
			throw p;
		}
	}
	/*@RequestMapping(value = "/product/push", method=RequestMethod.GET)
	@ResponseBody
	public String saveOrUpdateCategoryBase(@RequestBody String jsonParam) throws Exception {
		try{
			getProductExtends(jsonParam,RabbitReceivedDataType.RABBIT_CATEGORY_SAVE_UPDATE_CATEGORY_BASE_TYPE.getKey());
			return "successfully";
		} catch (Exception p) {
			log.error("Category saveOrUpdateCategoryBase error!",p);
			throw p;
		}
	}*/

	@RequestMapping(value = "/api/websitecategory/push", method=RequestMethod.POST)
	@ResponseBody
	public String platformPush(@RequestBody String jsonParam) throws Exception {
		try{
			getProductExtends(jsonParam,RabbitReceivedDataType.RABBIT_CATEGORY_PLATFORM_PUSH_TYPE.getKey());
			return "successfully";
		} catch (Exception p) {
			log.error("Category platformPush error!",p);
			throw p;
		}
	}
	/*@RequestMapping(value = "/product/push", method=RequestMethod.GET)
	@ResponseBody
	public String saveOrUpdatePlatformCategory(@RequestBody String jsonParam) throws Exception {
		try{
			Map<String,Object> map=new HashMap<String,Object>();
			map.put(RabbitReceivedDataType.RABBIT_CATEGORY_GET_TYPE.getKey(), jsonParam);
			sendService.sendMessage(map, productMqExtendsConfig.getRouteKey(), productMqExtendsConfig.getAmqpTemplate());
			
			return "success";
		} catch (Exception p) {
			log.error("Category saveOrUpdatePlatformCategory error!",p);
			throw p;
		}
	}*/
	@RequestMapping(value = "/api/websitecategory/{websiteid}/{languageid}", method=RequestMethod.GET)
	@ResponseBody
	public String get(@PathVariable("websiteid") Integer websiteid,
			@PathVariable("languageid") Integer languageid) throws Exception {
		try {
			Map<String,Object> paramMap=new HashMap<String,Object>();
			paramMap.put("websiteid", websiteid);
			paramMap.put("languageid", languageid);
			Map<String,Object> map=new HashMap<String,Object>();
			map.put(RabbitReceivedDataType.RABBIT_CATEGORY_GET_TYPE.getKey(), paramMap);
			sendService.sendMessage(map, productMqExtendsConfig.getRouteKey(), productMqExtendsConfig.getAmqpTemplate());
			
			return "success";
		} catch (Exception p) {
			log.error("attribute get error!",p);
			throw p;
		}
	}
	@RequestMapping(value = "/api/category/attribute", method=RequestMethod.GET)
	@ResponseBody
	public void getAllCategoryAttributes() {
		/*return ok(Json
				.toJson(categoryEnquiryService.getAllCategoryAttributes()));*///************************************
	}

	@RequestMapping(value = "/api/category/attribute", method=RequestMethod.POST)
	@ResponseBody
	public String saveCategoryAttributes(@RequestBody String jsonParam) throws Exception {
		try{
			getProductExtends(jsonParam,RabbitReceivedDataType.RABBIT_CATEGORY_SAVE_CATEGORY_ATTRIBUTES_TYPE.getKey());
			return "successfully";
		} catch (Exception p) {
			log.error("Category saveCategoryAttributes error!",p);
			throw p;
		}
	}
}
