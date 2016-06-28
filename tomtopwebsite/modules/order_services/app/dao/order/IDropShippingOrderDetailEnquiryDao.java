package dao.order;

import java.util.List;

import dao.IOrderEnquiryDao;
import dto.order.dropShipping.DropShippingOrderDetail;

public interface IDropShippingOrderDetailEnquiryDao extends IOrderEnquiryDao {
	List<DropShippingOrderDetail> getByDropShippingOrderID(Integer id);
}
