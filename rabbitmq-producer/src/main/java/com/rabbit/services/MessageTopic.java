package com.rabbit.services;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
/**
 * 优点：可以发送到多个队列，每个消息接收者，只需要接收自己感兴趣的消息即可
 * 
 * 使用场景：
 * 1》一种是考虑数据安全，保证数据不丢失，（消息没有消费，总会停留在队列，---可以设置过期时间）
 * 		补充：如果有两个监听者监听同一个队列，那么这个队列中的数据，就会被这两个监听者共同消费一份数据，
 * 2》一种是只广播消息，丢失情况不考虑，（服务器消息不会挤压）
 * 
 * 1：有预先建好的队列（队列最好是基于持久化的）
 * 	a:需要先弄好，并且建立好exchange与队列的绑定关系
 *  b:发送消息时，会自动接收消息，存入队列，防止消息丢失
 * 
 * 2：预先没有建好的队列
 * 	a:之前的消息都会丢失
 * 	b:服务启动后，才会监听消息，但建立的队列是rabbit自动生成的临时队列，消息传送有一定风险
 * @author Administrator
 *
 */
@Service
public class MessageTopic {
	private static Logger log=Logger.getLogger(MessageTopic.class.getName());
	
	private static final String MSG_TOPIC = "topic";
	private static final String SUCCESS = "success";
	private static final String FAIL = "fail";
	private static final String MESSAGE_FORMAT = "UTF-8";
	
	@Autowired
	ConnectionFactory rabbitFactory;

	public String sendTopicMsg(String exchangeName,String routingKey,String message) {
		Connection connection = null;
		Channel channel = null;
		String result=SUCCESS;
		try {

			connection = rabbitFactory.newConnection();
			channel = connection.createChannel();

			channel.exchangeDeclare(exchangeName, MSG_TOPIC);
			log.debug(" [sendTopicMsg] exchangeName:"+exchangeName+" routingKey:" + routingKey + "  message:" + message);
			channel.basicPublish(exchangeName, routingKey, null,message.getBytes(MESSAGE_FORMAT));

		} catch (Exception e) {
			 result=FAIL;
			 log.error("sendTopicMsg error:",e);
		} finally {
			if (connection != null && connection.isOpen()) {
				try {
					connection.close();
				} catch (Exception ignore) {
					 log.error("sendTopicMsg connection error:",ignore);
				}
			}
		}
		return result;
	}

}
