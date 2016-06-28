package com.rabbit.dao.daoImp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.rabbit.conf.ordermapper.base.ReceivedDataHistoryMapper;
import com.rabbit.dao.idao.IReceivedDataHistoryDao;
import com.rabbit.dto.order.ReceivedDataHistory;
@Component
public class ReceivedDataHistoryDao implements IReceivedDataHistoryDao {

	@Autowired
	ReceivedDataHistoryMapper receivedDataHistoryMapper;

	@Override
	public void addReceivedDataHistory(
			ReceivedDataHistory receivedDataHistory) {
		receivedDataHistoryMapper.addReceivedDataHistory(receivedDataHistory);
	}
}
