package com.rabbit.services;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;
/**
 * 采用键值对存储工厂
 * @author Administrator
 *
 */
@Service
public class CommonSendMGService {
	private static Logger log=Logger.getLogger(CommonSendMGService.class.getName());
	
	private static final  String SUCCESS="success"; 
	private static final  String FAIL="fail"; 
	@Autowired
	ConnectionFactory rabbitFactory;
	//消息发送
	public String commonSendMessage(String queryName,Object params){
		
		log.debug("commonSendMessage queryName:"+queryName+"  params:"+params);
		String result=SUCCESS;
        Channel channel =null;
        Connection connection=null;
		try {
			rabbitFactory.setVirtualHost("/");
			 connection = rabbitFactory.newConnection();
			 channel = connection.createChannel();  
			 // 声明队列  
			 boolean durable = true;// 1、设置队列持久化
			 // queue,  durable,  exclusive,  autoDelete, Map<String, Object> arguments
			 channel.queueDeclare(queryName, durable, false, false, null);  
			String message="";
			 if(params instanceof String){
				 message=(String) params;
				 // exchange,  routingKey, BasicProperties props, byte[] body
				 channel.basicPublish("", queryName,  
						 MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes());
			 }else if(params instanceof List){
				 @SuppressWarnings("rawtypes")
				List list=(List) params;
				 if(CollectionUtils.isNotEmpty(list)){
					 for(Object ob : list){
						 // exchange,  routingKey, BasicProperties props, byte[] body
						 if(ob==null){
							 continue;
						 }
						 String jsonString = JSON.toJSONString(ob);
						 channel.basicPublish("", queryName,  
								 MessageProperties.PERSISTENT_TEXT_PLAIN, jsonString.getBytes());
					 }
				 }
				 
			 }else{
				 message= JSON.toJSONString(params);
				 // exchange,  routingKey, BasicProperties props, byte[] body
				 channel.basicPublish("", queryName,  
						 MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes());
			 }
			 // 关闭频道和资源  
		} catch (IOException e) {
			result=FAIL;
			log.error("commonSendMessage error!",e);
		}finally{
			log.debug("commonSendMessage turn off collect");
				try {
					if(channel!=null && channel.isOpen()){
						channel.close();
					}
					if(connection!=null && connection.isOpen()){
						
						connection.close(); 
					}
				} catch (IOException e) {
					log.error("----------------->commonSendMessage channel or connection close error!",e);
				}  
		}  
        return result;
	}

	
	
}