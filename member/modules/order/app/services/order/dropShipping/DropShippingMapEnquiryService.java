package services.order.dropShipping;

import java.util.List;

import javax.inject.Inject;

import dao.order.IDropShippingMapEnquiryDao;
import dto.order.dropShipping.DropShippingMap;

public class DropShippingMapEnquiryService {
	@Inject
	private IDropShippingMapEnquiryDao enquiryDao;

	public List<DropShippingMap> getByDropShippingID(String id) {
		return enquiryDao.getListByDropShippingID(id);
	}

	public List<Integer> getDropShippingOrderIDsByShippingID(String id) {
		return enquiryDao.getDropShippingOrderIDsByDropShippingID(id);
	}

	public List<String> getOrderNumbersByID(String dropShippingID) {
		return enquiryDao.getOrderNumbersByID(dropShippingID);
	}
}
