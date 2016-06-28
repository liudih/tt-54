package dao.order;

import java.util.List;

import dao.IOrderUpdateDao;
import dto.order.dropShipping.DropShippingMap;

public interface IDropShippingMapUpdateDao extends IOrderUpdateDao {
	int batchInsert(List<DropShippingMap> list);

	int updateOrderNumber(String orderNumber, int dsOrderID);
}
