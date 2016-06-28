package com.rabbit.services.serviceImp.monitor;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rabbit.conf.mapper.monitor.MonitorRecordsMapper;
import com.rabbit.dto.monitor.RabbitMonitorDto;
/** mq监控类
* 1：记录所有经过mq队列的记录
* 2：记录每条可以确定唯一性记录的状态，成功或失败
* 3：对于失败的记录，需要通过唯一性标识的记录，有可以恢复的方法：（操作类型+记录唯一标识）例如：重新调用接口获取记录，
* 												    			       或者手动提取记录，重试推送记录
* 4：保存推送过来的报文，用于失败记录的分析
* 5:所有记录一律采用流水记录,用于统计
*/
@Service
public class MonitorThread implements Runnable {

	private static final int drainSize=100;
	private static final int threadSleepTime=1000*2;//2秒钟
	private static Logger log=Logger.getLogger(MonitorThread.class.getName());
	@Autowired
	MonitorRecordsMapper monitorRecordsMapper;
	@Override
	public void run() {
		 try{
			 while(true){
				 List<RabbitMonitorDto> rabbitMonitorList=MonitorTask.getInstance().getMonitorTask(drainSize);
				 if(CollectionUtils.isNotEmpty(rabbitMonitorList)){
					 log.info(Thread.currentThread().getName()+"----------->rabbitMonitorList:"+rabbitMonitorList.size());
					 monitorRecordsMapper.addMonitorRecords(rabbitMonitorList);
				 }
				 if(MonitorTask.getInstance().getQueueSize()<50){// 队列数据如果少于50，采用休眠，否则停止休眠
					 Thread.sleep(threadSleepTime); 
				 }
			 }
		 }catch(Exception ex){
			 log.error("--------------------->MonitorThread error!",ex);
		 }
	}


}
