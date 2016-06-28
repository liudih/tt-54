package com.rabbit.entry.rabbit.config;

import org.springframework.amqp.core.AmqpTemplate;


/**
 * Created by Thinkpad on 2015/8/4  22:21.
 */
public class ProductMqConfig {
	private AmqpTemplate amqpTemplate;
	private String routeKey;
	public ProductMqConfig(AmqpTemplate amqpTemplate,String routeKey){
		this.amqpTemplate=amqpTemplate;
		this.routeKey=routeKey;
	}
	public AmqpTemplate getAmqpTemplate() {
		return amqpTemplate;
	}
	public void setAmqpTemplate(AmqpTemplate amqpTemplate) {
		this.amqpTemplate = amqpTemplate;
	}
	public String getRouteKey() {
		return routeKey;
	}
	public void setRouteKey(String routeKey) {
		this.routeKey = routeKey;
	}
    
}
