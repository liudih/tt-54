package com.rabbit.services.serviceImp.mq.queue;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rabbitmq.client.*;
@Service
public class AskModeQuere {
	
	private static Logger log=Logger.getLogger(AskModeQuere.class.getName());
	  @Autowired
	  private ConnectionFactory rabbitFactory;
	  public  void receiveMessage(String taskQuereName) throws Exception {
		    final Connection connection = rabbitFactory.newConnection();
		    final Channel channel = connection.createChannel();

		    channel.queueDeclare(taskQuereName, true, false, false, null);

		    channel.basicQos(1);

		    final Consumer consumer = new DefaultConsumer(channel) {
		      @Override
		      public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
		        String message = new String(body, "UTF-8");

		        log.debug("CommonQuere handleDelivery message:"+message);
		        try {
		          doWork(message);
		        } finally {
		        	log.info("CommonQuere handleDelivery Ack message:"+message);
		          channel.basicAck(envelope.getDeliveryTag(), false);
		        }
		      }
		    };
		    channel.basicConsume(taskQuereName, false, consumer);
		  }

		  private static void doWork(String task) {
		    for (char ch : task.toCharArray()) {
		      if (ch == '.') {
		        /**try {
		          //Thread.sleep(1000);
		        } catch (InterruptedException _ignored) {
		          Thread.currentThread().interrupt();
		        }*/
		      }
		    }
		  }
	  
}

