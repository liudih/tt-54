package services.order.dropShipping;

import javax.inject.Inject;

import dao.order.IDropShippingOrderUpdateDao;
import dto.order.dropShipping.DropShippingOrder;

public class DropShippingOrderUpdateService {
	@Inject
	private IDropShippingOrderUpdateDao updateDao;

	public boolean insert(DropShippingOrder order) {
		int i = updateDao.insert(order);
		return i == 1 ? true : false;
	}
}
