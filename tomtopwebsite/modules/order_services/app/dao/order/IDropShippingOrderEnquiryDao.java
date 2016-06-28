package dao.order;

import java.util.List;

import dao.IOrderEnquiryDao;
import dto.order.dropShipping.DropShippingOrder;

public interface IDropShippingOrderEnquiryDao extends IOrderEnquiryDao {
	List<DropShippingOrder> getListByIDs(List<Integer> ids);
}
