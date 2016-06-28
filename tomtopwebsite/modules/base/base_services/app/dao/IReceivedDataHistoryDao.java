package dao;

import dao.IBaseEnquiryDao;
import dto.ReceivedDataHistory;

public interface IReceivedDataHistoryDao extends IBaseEnquiryDao {
	public void addReceivedDataHistory(ReceivedDataHistory receivedDataHistory);
}
