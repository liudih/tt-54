package com.rabbit.services;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rabbit.services.serviceImp.mq.queue.AskModeQuere;
import com.rabbit.services.serviceImp.mq.topic.ReceiveMessageTopic;
import com.rabbit.services.serviceImp.mq.topic.ReceiveMessageTopic2;
import com.rabbit.services.serviceImp.mq.topic.ReceiveMessageTopicBack;
import com.rabbit.services.serviceImp.mq.topic.ReceiveMessageAll;
@Service
public class RabbitQueueManager{
	Logger log = Logger.getLogger(RabbitQueueManager.class.getName());
	private static final String COMMON_QUEUE_NAME="common_queue";
	
	private static final String TOPTIC_EXCHANGE_NEWS="EX1";
	private static final String TOPTIC_ROUTE_KEY_NEWS="tomtop.message.routingkey";
	private static final String TOPTIC_QUEUE_NEWS="tomtop.message";
	
	private static final String TOPTIC_QUEUE_NEWS2="tomtopLog";
	private static final String TOPTIC_ROUTE_KEY_NEWS2="tomtop.message.routingkey2";
	
	private static final String TOPTIC_ROUTE_KEY_ALL="tomtop.#"; //   *一个
	@Autowired
	private AskModeQuere askModeQuere;
	@Autowired
	ReceiveMessageTopic receiveMessageTopic;
	@Autowired
	ReceiveMessageTopicBack receiveMessageTopicBack;
	
	@Autowired
	ReceiveMessageTopic2 receiveMessageTopic2;
	
	@Autowired
	ReceiveMessageAll receiveMessageTotal;
	public void startQueueListener() throws Exception{
		askModeQuere.receiveMessage(COMMON_QUEUE_NAME);
		receiveMessageTopic.topMessageHandle(TOPTIC_QUEUE_NEWS,TOPTIC_EXCHANGE_NEWS, TOPTIC_ROUTE_KEY_NEWS);
		receiveMessageTopicBack.topMessageHandle(TOPTIC_QUEUE_NEWS,TOPTIC_EXCHANGE_NEWS, TOPTIC_ROUTE_KEY_NEWS);
		
		
		receiveMessageTopic2.topMessageHandle(TOPTIC_QUEUE_NEWS2,TOPTIC_EXCHANGE_NEWS, TOPTIC_ROUTE_KEY_NEWS2);
	
		receiveMessageTotal.topMessageHandle(TOPTIC_EXCHANGE_NEWS, TOPTIC_ROUTE_KEY_ALL);
	}
}
