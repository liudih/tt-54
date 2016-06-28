package com.rabbit.services.serviceImp.monitor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rabbit.conf.mapper.monitor.MonitorRecordsMapper;
import com.rabbit.dto.monitor.RabbitMonitorDto;
import com.rabbit.services.iservice.monitor.IMonitorService;
/** mq监控类
* 1：记录所有经过mq队列的记录
* 2：记录每条可以确定唯一性记录的状态，成功或失败
* 3：对于失败的记录，需要通过唯一性标识的记录，有可以恢复的方法：（操作类型+记录唯一标识）例如：重新调用接口获取记录，
* 												    			       或者手动提取记录，重试推送记录
* 4：保存推送过来的报文，用于失败记录的分析
* 5:所有记录一律采用流水记录,用于统计
*/
@Service
public class MonitorService implements IMonitorService {

	@Autowired
	MonitorRecordsMapper monitorRecordsMapper;
	
	@Override
	public void addMonitorRecord(RabbitMonitorDto rabbitMonitorDto) {
		monitorRecordsMapper.addMonitorRecord(rabbitMonitorDto);
//		MonitorTask.getInstance().addMonitorTask(rabbitMonitorDto);
	}
	
}
