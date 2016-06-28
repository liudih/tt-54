package dao.order;

import java.util.List;

import dao.IOrderUpdateDao;
import dto.order.dropShipping.DropShippingOrderDetail;

public interface IDropShippingOrderDetailUpdateDao extends IOrderUpdateDao {
	int batchInsert(List<DropShippingOrderDetail> list);
}
