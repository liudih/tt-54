package dao.order.impl;

import java.util.List;

import javax.inject.Inject;

import mapper.order.DropShippingOrderMapper;
import dao.order.IDropShippingOrderEnquiryDao;
import dto.order.dropShipping.DropShippingOrder;

public class DropShippingOrderEnquiryDao implements
		IDropShippingOrderEnquiryDao {
	@Inject
	private DropShippingOrderMapper mapper;

	@Override
	public List<DropShippingOrder> getListByIDs(List<Integer> ids) {
		return mapper.getListByIDs(ids);
	}

}
