package com.rabbit.controller;

import java.util.HashMap;
import java.util.Map;

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
public class Keyword {
	private static Logger log = Logger.getLogger(Keyword.class.getName());
	@Autowired
	SendService sendService;
	@Autowired
	ProductMqExtendsConfig productMqExtendsConfig;

	/**
	 * 保存搜索关键词
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/api/product/keyword/push", method = RequestMethod.POST)
	@ResponseBody
	public String push(@RequestBody String jsonParam) throws Exception {
		try {
			log.debug("----" + jsonParam);
			ObjectMapper om = new ObjectMapper();
			JsonNode node = om.readTree(jsonParam);
			if (node == null) {
				throw new Exception("Expecting Json data");
			}
			Map<String, Object> map = new HashMap<String, Object>();
			map.put(RabbitReceivedDataType.RABBIT_KEYWORD_PUSH_TYPE.getKey(),
					node);
			sendService.sendMessage(map, productMqExtendsConfig.getRouteKey(),
					productMqExtendsConfig.getAmqpTemplate());
			return "successfully";
		} catch (Exception p) {
			log.error("Keyword  push error!", p);
			throw p;
		}
	}
}