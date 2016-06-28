package com.rabbit.temp;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.rabbitmq.client.Channel;  
import com.rabbitmq.client.Connection;  
import com.rabbitmq.client.ConnectionFactory;  
import com.rabbitmq.client.ConsumerCancelledException;
import com.rabbitmq.client.QueueingConsumer; 
import com.rabbitmq.client.ShutdownSignalException;


public class CommonQueueLitener{
	private static Logger log=Logger.getLogger(CommonQueueLitener.class.getName());
	
	@Autowired
	private ConnectionFactory connectionFactory;
	public void receiveMessage(String queueName ) throws IOException{
		
		try {
			// 创建连接和频道  
			Connection connection = connectionFactory.newConnection();
			Channel channel = connection.createChannel();  
			// 声明队列  
			boolean durable = true;  
			channel.queueDeclare(queueName, durable, false, false, null);  
			//设置最大服务转发消息数量  
			int prefetchCount = 1;  
			channel.basicQos(prefetchCount);  
			QueueingConsumer consumer = new QueueingConsumer(channel);  
			// 指定消费队列  
			boolean ack = false; // 打开应答机制  
			channel.basicConsume(queueName, ack, consumer);  
			while (true)  
			{  
				try {
					QueueingConsumer.Delivery delivery = consumer.nextDelivery();
					String message = new String(delivery.getBody());  
					
					log.debug("CommonQueueLitener  Received :" + message );  
					if(task(message)){
						
						channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);  //设置应答
					}else{
						log.error("receiveMessage no ack message:"+message);
					}
				} catch (ShutdownSignalException e) {
					log.error("------------receiveMessage  error1",e);
				} catch (ConsumerCancelledException e) {
					log.error("------------receiveMessage  error2",e);
				} catch (InterruptedException e) {
					log.error("------------receiveMessage  error3",e);
				}  
				
			}  
		} catch (IOException e) {
			log.error("------------receiveMessage  error3",e);
			throw e;
		}  
	}
	private boolean task(String message){
		boolean result=false;
		
		
		result=true;
		return result;
		
	}
	
}
