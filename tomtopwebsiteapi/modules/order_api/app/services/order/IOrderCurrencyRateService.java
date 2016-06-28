package services.order;

import dto.order.OrderCurrencyRate;

public interface IOrderCurrencyRateService {

	public abstract OrderCurrencyRate getByOrderNumber(String orderNumber);

	public abstract boolean insert(OrderCurrencyRate rate);

}