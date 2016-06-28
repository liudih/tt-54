package services.order.dropShipping;

import java.util.List;

import javax.inject.Inject;

import dao.order.IDropShippingMapUpdateDao;
import dto.order.dropShipping.DropShippingMap;

public class DropShippingMapUpdateService {
	@Inject
	private IDropShippingMapUpdateDao updateDao;

	public boolean batchInsert(List<DropShippingMap> list) {
		if (list == null || list.isEmpty()) {
			return true;
		}
		int i = updateDao.batchInsert(list);
		return i == list.size() ? true : false;
	}

	public boolean updateOrderNumber(String orderNumber, int dsOrderID) {
		int i = updateDao.updateOrderNumber(orderNumber, dsOrderID);
		return i == 1 ? true : false;
	}
}
