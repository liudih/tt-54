package dao.order.impl;

import java.util.List;

import javax.inject.Inject;

import mapper.order.DropShippingMapMapper;
import dao.order.IDropShippingMapEnquiryDao;
import dto.order.dropShipping.DropShippingMap;

public class DropShippingMapEnquiryDao implements IDropShippingMapEnquiryDao {
	@Inject
	private DropShippingMapMapper mapper;

	@Override
	public List<DropShippingMap> getListByDropShippingID(String id) {
		return mapper.getByDropshippingID(id);
	}

	@Override
	public List<Integer> getDropShippingOrderIDsByDropShippingID(String id) {
		return mapper.getDropShippingOrderIDByDropshippingID(id);
	}

	@Override
	public List<String> getOrderNumbersByID(String dropShippingID) {
		return mapper.getOrderNumbersByID(dropShippingID);
	}

}
