package com.rabbit.services;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

/**
 * Created by Thinkpad on 2015/8/4  22:00.
 */
public class ProducerConfiguration {
    private String queueName;
    private String routingKey;
    private RabbitTemplate rabbitTemplate;
    private String exchange;
    private  CachingConnectionFactory connectionFactory;
    public ProducerConfiguration() {

    }
    public ProducerConfiguration(String exchange,String queueName, String routingKey) {
        this.queueName = queueName;
        this.routingKey = routingKey;
        this.exchange=exchange;
        this.rabbitTemplate = rabbitTemplate();
        RabbitAdmin admin = new RabbitAdmin(this.rabbitTemplate.getConnectionFactory());
        admin.declareQueue(new Queue(this.queueName));
        admin.declareExchange(new TopicExchange(exchange));
        admin.setAutoStartup(true);
        
    }

    public String getExchange() {
        return exchange;
    }

    public void setExchange(String exchange) {
        this.exchange = exchange;
    }

    public void setQueueName(String queueName) {
        this.queueName = queueName;
    }

    public void setRoutingKey(String routingKey) {
        this.routingKey = routingKey;
    }
    public String getQueueName() {
        return queueName;
    }

    public String getRoutingKey() {
        return routingKey;
    }
    public RabbitTemplate rabbitTemplate() {
    	connectionFactory = CachingConnectionFactoryManager.getCachingConnectionFactory();
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setRoutingKey(this.routingKey);
        template.setQueue(this.queueName);
        return template;
    }

   
   

    public void send(String s) {

        this.rabbitTemplate.convertAndSend(s);
    }

    public void send(String exchange,String routingKey,Object msg) {

        this.rabbitTemplate.convertAndSend(exchange,routingKey,msg);
    }
}
