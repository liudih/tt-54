package dao.order;

import dao.IOrderEnquiryDao;
import dto.order.OrderCurrencyRate;

public interface IOrderCurrencyRateEnquiryDao extends IOrderEnquiryDao {
	OrderCurrencyRate getByOrderNumber(String orderNumber);
}
