package dao.impl;

import mapper.base.ReceivedDataHistoryMapper;

import com.google.inject.Inject;

import dao.IReceivedDataHistoryDao;

public class ReceivedDataHistoryDao implements IReceivedDataHistoryDao {

	@Inject
	ReceivedDataHistoryMapper receivedDataHistoryMapper;

	@Override
	public void addReceivedDataHistory(
			dto.ReceivedDataHistory receivedDataHistory) {
		receivedDataHistoryMapper.addReceivedDataHistory(receivedDataHistory);
	}
}
