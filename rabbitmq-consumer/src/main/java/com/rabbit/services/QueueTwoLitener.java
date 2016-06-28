package com.rabbit.services;

import java.io.UnsupportedEncodingException;

import org.apache.log4j.Logger;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;

import com.alibaba.fastjson.JSON;
import com.rabbit.dto.Person;


public class QueueTwoLitener implements MessageListener{
	Logger log=Logger.getLogger(QueueTwoLitener.class.getName());
	public void onMessage(Message message) {
		
		String json;
		try {
			json = new String(message.getBody(),"UTF-8");
			
			Person parse=JSON.parseObject(json, Person.class);
			log.info("person :" +parse );
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
    } 
	
}
