package services.order.dropShipping;

import java.util.List;

import javax.inject.Inject;

import dao.order.IDropShippingOrderDetailUpdateDao;
import dto.order.dropShipping.DropShippingOrderDetail;

public class DropShippingOrderDetailUpdateService {
	@Inject
	private IDropShippingOrderDetailUpdateDao updateDao;

	public boolean batchInsert(List<DropShippingOrderDetail> list, int orderID) {
		if (list == null || list.isEmpty()) {
			return true;
		}
		list.forEach(d -> d.setIorderid(orderID));
		int i = updateDao.batchInsert(list);
		return i == list.size() ? true : false;
	}
}
