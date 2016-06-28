package com.rabbit.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rabbit.services.CommonSendMGService;
import com.rabbit.services.MessageTopic;

@Controller
public class CommonSendMGContoller {
	
	@Autowired
	private CommonSendMGService commonSendMessage;
	
	@Autowired
	private MessageTopic messageTopic;
	/**
	 * 修改订单状态为Dispatched
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/mq/queueAskMessageMode", method = { RequestMethod.GET })
	@ResponseBody
	public String queueAskMessageMode(String queryName,String  params) {

			return commonSendMessage.commonSendMessage(queryName, params);
	}
	
	@RequestMapping(value = "/mq/topMessageMode", method = { RequestMethod.GET })
	@ResponseBody
	public String topMessageMode(String exchangeName,String  routingKey,String message) {

		for(int i=0;i<5;i++){
			messageTopic.sendTopicMsg(exchangeName, routingKey, message+"------------->"+i);
		}
		return "success";
	}
	
}
