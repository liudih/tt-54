package dao.order.impl;

import javax.inject.Inject;

import mapper.order.OrderCurrencyRateMapper;
import dao.order.IOrderCurrencyRateUpdateDao;
import dto.order.OrderCurrencyRate;

public class OrderCurrencyRateUpdateDao implements IOrderCurrencyRateUpdateDao {
	@Inject
	OrderCurrencyRateMapper mapper;

	@Override
	public int insert(OrderCurrencyRate rate) {
		return mapper.insert(rate);
	}

}
