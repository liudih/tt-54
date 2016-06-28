package services.base;

import com.google.inject.Inject;

import dao.IReceivedDataHistoryDao;
import dto.ReceivedDataHistory;

public class ReceivedDataHistoryService {

	@Inject
	IReceivedDataHistoryDao receivedDataHistoryDao;

	public void addReceivedDataHistory(ReceivedDataHistory receivedDataHistory) {
		receivedDataHistoryDao.addReceivedDataHistory(receivedDataHistory);
	}
}
