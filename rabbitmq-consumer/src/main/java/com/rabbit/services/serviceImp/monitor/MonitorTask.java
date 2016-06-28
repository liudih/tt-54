package com.rabbit.services.serviceImp.monitor;

import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import org.apache.log4j.Logger;

import com.google.common.collect.Lists;
import com.rabbit.dto.monitor.RabbitMonitorDto;
/**
 * 监控任务类
 *  1：可单个添加任务，
 *  2：批量取走任务
 * @author Administrator
 *
 */
public class MonitorTask {
	private static Logger log=Logger.getLogger(MonitorTask.class.getName());
	private static final BlockingQueue<RabbitMonitorDto> queue = 
					new ArrayBlockingQueue<RabbitMonitorDto>(3000);//一条记录5kX3000 接近50M
	private static MonitorTask monitorTask;
	private MonitorTask(){
		
	}
	
	public static MonitorTask getInstance(){
		if(monitorTask==null){
			monitorTask=new MonitorTask();
		}
		return monitorTask;
	}
	public int getQueueSize(){
		return queue.size();
	}
	public void addMonitorTask(RabbitMonitorDto rabbitMonitorDto){
		queue.add(rabbitMonitorDto);
	}
	public List<RabbitMonitorDto> getMonitorTask(int size){
		if(size<1 || queue.size()==0){
			return Lists.newArrayList();
		}
		List<RabbitMonitorDto> list=Lists.newArrayList();
		int currentQueueSize=queue.size();
		if(currentQueueSize>size){
			queue.drainTo(list, size);
		}else{
			queue.drainTo(list, currentQueueSize);
		}
		log.error("getMonitorTask currentQueueSize:"+currentQueueSize+
				"  list.size:"+list.size()+"  over length:"+(currentQueueSize-list.size()));
		return list;
	}
}
