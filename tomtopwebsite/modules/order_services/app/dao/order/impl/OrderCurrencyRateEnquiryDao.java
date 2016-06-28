package dao.order.impl;

import javax.inject.Inject;

import mapper.order.OrderCurrencyRateMapper;
import dao.order.IOrderCurrencyRateEnquiryDao;
import dto.order.OrderCurrencyRate;

public class OrderCurrencyRateEnquiryDao implements
		IOrderCurrencyRateEnquiryDao {
	@Inject
	OrderCurrencyRateMapper mapper;

	@Override
	public OrderCurrencyRate getByOrderNumber(String orderNumber) {
		return mapper.getByOrderNumber(orderNumber);
	}

}
