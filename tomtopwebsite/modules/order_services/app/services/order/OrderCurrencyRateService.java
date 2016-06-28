package services.order;

import java.util.List;

import javax.inject.Inject;

import mapper.order.OrderCurrencyRateMapper;
import dao.order.IOrderCurrencyRateEnquiryDao;
import dao.order.IOrderCurrencyRateUpdateDao;
import dto.order.OrderCurrencyRate;

public class OrderCurrencyRateService implements IOrderCurrencyRateService {
	@Inject
	IOrderCurrencyRateUpdateDao updateDao;
	@Inject
	IOrderCurrencyRateEnquiryDao enquiryDao;
	@Inject
	OrderCurrencyRateMapper orderCurrencyRateMapper;

	/* (non-Javadoc)
	 * @see services.order.IOrderCurrencyRateService#getByOrderNumber(java.lang.String)
	 */
	@Override
	public OrderCurrencyRate getByOrderNumber(String orderNumber) {
		return enquiryDao.getByOrderNumber(orderNumber);
	}

	/* (non-Javadoc)
	 * @see services.order.IOrderCurrencyRateService#insert(dto.order.OrderCurrencyRate)
	 */
	@Override
	public boolean insert(OrderCurrencyRate rate) {
		int i = updateDao.insert(rate);
		return 1 == i ? true : false;
	}
	
	public List<OrderCurrencyRate> getRateByOrderNumbers(List<String> olist){
		return orderCurrencyRateMapper.getRateByOrderNumbers(olist);
	}
}
