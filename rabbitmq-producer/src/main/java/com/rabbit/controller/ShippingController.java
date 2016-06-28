package com.rabbit.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.rabbit.entry.ProductBack;
import com.rabbit.entry.rabbit.config.OrderMqConfig;
import com.rabbit.enums.RabbitReceivedDataType;
import com.rabbit.services.SendService;
@Controller
public class ShippingController {

	private static Logger log=Logger.getLogger(ShippingController.class.getName());
	 
	@Autowired
	SendService sendService;
	
	@Autowired
	OrderMqConfig orderConfig;
	
	@RequestMapping(value = "/checkout/api/shippingmethod/push", method=RequestMethod.POST)
	@ResponseBody
	public String push(@RequestBody String jsonParam) throws Exception {
		
		log.debug("ShippingController push jsonParam:"+jsonParam);
		
		if (StringUtils.isEmpty(jsonParam)) {
			throw new Exception("params empty:"+jsonParam);
		}
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		jsonMap.put(RabbitReceivedDataType.RABBIT_SHIPPING_PUSH_TYPE.getKey(), jsonParam);
		
		sendService.sendMessage(jsonMap, orderConfig.getRouteKey(),
				orderConfig.getAmqpTemplate());
		return "true";
	}
}
