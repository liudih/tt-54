package com.rabbit.services;

import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;

/**
 * Created by Thinkpad on 2015/8/4  22:04.
 */
public class ConsumerSimpleMessageListenerContainer extends SimpleMessageListenerContainer {

	public ConsumerSimpleMessageListenerContainer(){
		
	}
    public void startConsumers() throws Exception {
        super.doStart();
    }

}

