package com.rabbit.controller;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rabbit.entry.rabbit.config.ProductMqExtendsConfig;
import com.rabbit.enums.RabbitReceivedDataType;
import com.rabbit.services.SendService;

/*@ApiPermission*/
@Controller
public class ThirdPlatformData{
	private static Logger log=Logger.getLogger(ThirdPlatformData.class.getName());
	@Autowired
	SendService sendService;
	@Autowired
	ProductMqExtendsConfig productMqExtendsConfig;
	
	/*@ApiHistory(type = HandleReceivedDataType.ADD_THIRDPLATFORMDATA, createuser = "api")*/
	@RequestMapping(value = "/api/product/thirdplatformdata/push", method=RequestMethod.POST)
	@ResponseBody
	public String push(@RequestBody String jsonParam) throws Exception {
		try {
			if (StringUtils.isEmpty(jsonParam)) {
				throw new Exception("Expecting Json data");
			}
			Map<String,Object> map=new HashMap<String,Object>();
			map.put(RabbitReceivedDataType.RABBIT_THIREDPLATFORMDATE_PUSH_TYPE.getKey(), jsonParam);
			sendService.sendMessage(map, productMqExtendsConfig.getRouteKey(), productMqExtendsConfig.getAmqpTemplate());
			return "success";
		} catch (Exception e) {
			e.printStackTrace();
			log.debug("push ThirdPlatformData error: " + e.getMessage());
			throw e;
		}
	}
}
