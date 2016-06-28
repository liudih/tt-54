package com.rabbit.services;

import org.apache.log4j.Logger;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;

/**
 * Created by Thinkpad on 2015/8/4  22:05.
 */
public class ConsumerConfig {

	Logger log=Logger.getLogger(ConsumerConfig.class.getName());
    private String queueName;
    private String routingKey;
    private int onOfConsumer;
    private  CachingConnectionFactory connectionFactory;
	public int getOnOfConsumer() {
        return onOfConsumer;
    }

    public void setOnOfConsumer(int onOfConsumer) {
        this.onOfConsumer = onOfConsumer;
    }

    public String getQueueName() {
        return queueName;
    }

    public void setQueueName(String queueName) {
        this.queueName = queueName;
    }

    public String getRoutingKey() {
        return routingKey;
    }

    public void setRoutingKey(String routingKey) {
        this.routingKey = routingKey;
    }

    //public void addListenService(ConsumerSimpleMessageListenerContainer container){
    public void addListenService(){
    	
    }
    public ConsumerConfig(String queueName, String routingKey, int onOfConsumer) throws Exception {
    	//if(container!=null){
    		if(connectionFactory==null){
    			connectionFactory = CachingConnectionFactoryManager.getCachingConnectionFactory();
    		}
    		ConsumerSimpleMessageListenerContainer container=new ConsumerSimpleMessageListenerContainer();
    		container.setConnectionFactory(connectionFactory);
    		container.setQueueNames(queueName);
    		container.setConcurrentConsumers(onOfConsumer);
    		container.setMessageListener(new MessageListenerAdapter(new ConsumerHandler()));
    		try {
    			container.startConsumers();
    		} catch (Exception e) {
    			log.error("addListenService error:",e);
    		}
    		
    	//}else{
    	//	log.error("addListenService null");
    	//}
    }
   // }
   
}