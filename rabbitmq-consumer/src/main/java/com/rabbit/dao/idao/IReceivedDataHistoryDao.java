package com.rabbit.dao.idao;

import com.rabbit.dto.order.ReceivedDataHistory;

public interface IReceivedDataHistoryDao extends IBaseEnquiryDao {
	public void addReceivedDataHistory(ReceivedDataHistory receivedDataHistory);
}
