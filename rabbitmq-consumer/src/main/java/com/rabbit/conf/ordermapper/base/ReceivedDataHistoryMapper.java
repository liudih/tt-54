package com.rabbit.conf.ordermapper.base;

import org.apache.ibatis.annotations.Insert;

import com.rabbit.dto.order.ReceivedDataHistory;

public interface ReceivedDataHistoryMapper {
	@Insert("insert into t_received_data_history (ctype, ccontent,ccreateuser) values(#{ctype},#{ccontent},#{ccreateuser})")
    int addReceivedDataHistory(ReceivedDataHistory record);
}