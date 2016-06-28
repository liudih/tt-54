package dao.order.impl;

import javax.inject.Inject;

import mapper.order.OrderAlterHistoryMapper;
import dao.order.IOrderAlterHistoryUpdateDao;
import dto.order.OrderAlterHistory;

public class OrderAlterHistoryUpdateDao implements IOrderAlterHistoryUpdateDao {
	@Inject
	OrderAlterHistoryMapper orderAlterHistoryMapper;

	public int insert(OrderAlterHistory orderAlterHistory) {
		return orderAlterHistoryMapper.insert(orderAlterHistory);
	}

}
