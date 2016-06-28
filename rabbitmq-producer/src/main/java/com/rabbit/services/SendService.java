package com.rabbit.services;

import org.apache.log4j.Logger;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.util.StringUtils;
/*@Service("sendService")*/
public class SendService {
	private static Logger log=Logger.getLogger(SendService.class.getName());
	/**
	 * 只发送json格式数据
	 * @param obj
	 */
	public void  sendMessage(Object obj,  String routeKey,AmqpTemplate amqpTemplate) {
		if(StringUtils.isEmpty(routeKey ) || obj==null || amqpTemplate==null){
			return;
		}
		int count=1;
		while(count>0){
			try{
				
				if(count>3){//如果发生异常，则
					count=0;
				}
				if(StringUtils.isEmpty(routeKey)){
					amqpTemplate.convertAndSend(obj);
				}else{
					amqpTemplate.convertAndSend(routeKey,obj);
				}
				//一次成功后退出
				log.info("SendService sendMessage success");
				count=0;
			}catch(Exception e){
				log.error("SendService sendMessage error!",e);
				if(count>2){//这个地方控制循环次数3次异常尝试，失败后，直接丢弃记录发送
					count=0;
					log.info("SendService sendMessage Discard records");
				}else{
					count++;
				}
			}
		}
	}  
}