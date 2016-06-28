package dao.order.impl;

import java.util.List;

import javax.inject.Inject;

import mapper.order.DropShippingOrderDetailMapper;
import dao.order.IDropShippingOrderDetailEnquiryDao;
import dto.order.dropShipping.DropShippingOrderDetail;

public class DropShippingOrderDetailEnquiryDao implements
		IDropShippingOrderDetailEnquiryDao {
	@Inject
	private DropShippingOrderDetailMapper mapper;

	@Override
	public List<DropShippingOrderDetail> getByDropShippingOrderID(Integer id) {
		return mapper.getByDropShippingOrderID(id);
	}

}
