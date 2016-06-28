package com.rabbit.services.iservice.monitor;


import com.rabbit.dto.monitor.RabbitMonitorDto;


public interface IMonitorService {
	
	public void addMonitorRecord( RabbitMonitorDto rabbitMonitorDto);
}