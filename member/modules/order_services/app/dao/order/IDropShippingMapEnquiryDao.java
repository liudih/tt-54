package dao.order;

import java.util.List;

import dao.IOrderEnquiryDao;
import dto.order.dropShipping.DropShippingMap;

public interface IDropShippingMapEnquiryDao extends IOrderEnquiryDao {
	List<DropShippingMap> getListByDropShippingID(String id);

	List<Integer> getDropShippingOrderIDsByDropShippingID(String id);

	List<String> getOrderNumbersByID(String dropShippingID);
}
