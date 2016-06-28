package dao.order;

import dao.IOrderUpdateDao;
import dto.order.OrderCurrencyRate;

public interface IOrderCurrencyRateUpdateDao extends IOrderUpdateDao {
	int insert(OrderCurrencyRate rate);
}
