package services.order.dropShipping;

import java.util.List;

import javax.inject.Inject;

import dao.order.IDropShippingOrderEnquiryDao;
import dto.order.dropShipping.DropShippingOrder;

public class DropShippingOrderEnquiryService {
	@Inject
	private IDropShippingOrderEnquiryDao enquiryDao;

	public List<DropShippingOrder> getListByIDs(List<Integer> ids) {
		return enquiryDao.getListByIDs(ids);
	}
}
