package com.rabbit.services;

import org.apache.log4j.Logger;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;

import com.rabbit.util.ObjectConverterUtils;

public class QueueOneLitener implements MessageListener{
	Logger log=Logger.getLogger(QueueOneLitener.class.getName());
	public void onMessage(Message message) {
		
		try {
			Thread.sleep(800);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		byte[] body = message.getBody();
		String json =(String) ObjectConverterUtils.ByteToObject(body);
		log.info("data :" +json );
        System.out.println("data :" + json);
    } 
	
}
