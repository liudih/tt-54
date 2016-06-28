package services.order.dropShipping;

import java.util.List;

import javax.inject.Inject;

import dao.order.IDropShippingOrderDetailEnquiryDao;
import dto.order.dropShipping.DropShippingOrderDetail;

public class DropShippingOrderDetailEnquiryService {
	@Inject
	private IDropShippingOrderDetailEnquiryDao enquiryDao;

	public List<DropShippingOrderDetail> getByDropShippingOrderID(Integer id) {
		return enquiryDao.getByDropShippingOrderID(id);
	}
}
