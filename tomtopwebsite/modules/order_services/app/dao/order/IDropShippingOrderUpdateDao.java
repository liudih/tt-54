package dao.order;

import dao.IOrderUpdateDao;
import dto.order.dropShipping.DropShippingOrder;

public interface IDropShippingOrderUpdateDao extends IOrderUpdateDao {
	int insert(DropShippingOrder order);
}
