package com.rabbit.services;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rabbit.entry.RabbitMonitorDto;
import com.rabbit.mapper.MonitorRecordsMapper;
import com.rabbit.util.DateFormatUtils;

@Service
public class MonitorService{

	@Autowired
	MonitorRecordsMapper monitorRecordsMapper;
	
	public void addMonitorRecord(RabbitMonitorDto rabbitMonitorDto) {
		if(rabbitMonitorDto==null){
			return;
		}else{
			String date=DateFormatUtils.getUtcDateStr(new Date());
			rabbitMonitorDto.setCreatedOn(DateFormatUtils.getUtcDateStr(new Date()));
			rabbitMonitorDto.setCreatedBy("product_api");
			rabbitMonitorDto.setIsDeleted("0");
			rabbitMonitorDto.setLastUpdatedBy("product_api");
			rabbitMonitorDto.setLastUpdatedOn(date);
			String failReason=rabbitMonitorDto.getFailReason();
			if (StringUtils.isNotEmpty(failReason)) {
				failReason = failReason.length() > 5000 ? failReason.substring(0,
						5000) : failReason;
			}
		}
		monitorRecordsMapper.addMonitorRecord(rabbitMonitorDto);
	}
	
}
