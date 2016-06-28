package com.rabbit.services.serviceImp.mq.topic;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
@Service
public class ReceiveMessageTopic {
	Logger log = Logger.getLogger(ReceiveMessageTopic.class.getName());
	
	private static final String MESSAGE_TOPIC = "topic";
	
	@Autowired
	private ConnectionFactory rabbitFactory;
	
	public void topMessageHandle(String queueName,String exchangeName,String routeKey) throws Exception {
		Connection connection = rabbitFactory.newConnection();
		Channel channel = connection.createChannel();

		channel.exchangeDeclare(exchangeName, MESSAGE_TOPIC);
		//String queueName = channel.queueDeclare().getQueue();
		channel.queueBind(queueName, exchangeName, routeKey);

		Consumer consumer = new DefaultConsumer(channel) {
			@Override
			public void handleDelivery(String consumerTag, Envelope envelope,
					AMQP.BasicProperties properties, byte[] body)
					throws IOException {
				String message = new String(body, "UTF-8");
				log.info("ReceiveMessageTopic topMessageHandle  RoutingKey:" + envelope.getRoutingKey()+"  queueName:"+queueName
					+"   exchangeName:"+exchangeName	+ "':'" + message);
			}
		};
		channel.basicConsume(queueName, true, consumer);//自动确认方式
	}
}
