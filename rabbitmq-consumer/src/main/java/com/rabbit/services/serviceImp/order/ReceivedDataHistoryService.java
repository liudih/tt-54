package com.rabbit.services.serviceImp.order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rabbit.dao.idao.IReceivedDataHistoryDao;
import com.rabbit.dto.order.ReceivedDataHistory;
@Service
public class ReceivedDataHistoryService {

	@Autowired
	IReceivedDataHistoryDao receivedDataHistoryDao;

	public void addReceivedDataHistory(ReceivedDataHistory receivedDataHistory) {
		receivedDataHistoryDao.addReceivedDataHistory(receivedDataHistory);
	}
}
