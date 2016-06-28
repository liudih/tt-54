package com.rabbit.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.rabbit.entry.RabbitMonitorDto;
public interface MonitorRecordsMapper {

	public void addMonitorRecords(
			@Param("list") List<RabbitMonitorDto> rabbitMonitorList);
	
	public void addMonitorRecord(
			@Param("rabbitMonitor") RabbitMonitorDto rabbitMonitor);
	
	
	@Select("  select moniter_id moniterId,record_key recordKey,opt_type optType,record_state recordState, "
		+"node_data nodeData,fail_reason failReason,created_on createdOn,created_by createdBy,last_updated_on lastUpdatedOn,last_updated_by lastUpdatedBy,"
			+"is_deleted isDeleted from t_rabbit_monitor"
			+ "  LIMIT #{1} offset (#{0}-1)*#{1} ")
	public List<RabbitMonitorDto> queryMonitorRecords(int page,int pageSize);

}
