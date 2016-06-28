package dao.order.impl;

import javax.inject.Inject;

import mapper.order.OrderAlterHistoryMapper;
import dao.order.IOrderAlterHistoryEnquiryDao;
import dto.order.OrderAlterHistory;

public class OrderAlterHistoryEnquiryDao implements
		IOrderAlterHistoryEnquiryDao {
	@Inject
	OrderAlterHistoryMapper orderAlterHistoryMapper;

	@Override
	public OrderAlterHistory getOrderAlterHistoryById(Integer iid) {
		return orderAlterHistoryMapper.getOrderAlterHistoryById(iid);
	}

	@Override
	public OrderAlterHistory getEarliestByOrder(Integer orderId) {
		return orderAlterHistoryMapper.getEarliestByOrder(orderId);
	}
}
