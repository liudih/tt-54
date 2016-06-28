package dao.order.impl;

import javax.inject.Inject;

import mapper.order.DropShippingOrderMapper;
import dao.order.IDropShippingOrderUpdateDao;
import dto.order.dropShipping.DropShippingOrder;

public class DropShippingOrderUpdateDao implements IDropShippingOrderUpdateDao {
	@Inject
	private DropShippingOrderMapper mapper;

	@Override
	public int insert(DropShippingOrder order) {
		return mapper.insert(order);
	}

}
