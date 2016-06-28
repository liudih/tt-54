package dao.order.impl;

import java.util.List;

import javax.inject.Inject;

import mapper.order.DropShippingOrderDetailMapper;
import dao.order.IDropShippingOrderDetailUpdateDao;
import dto.order.dropShipping.DropShippingOrderDetail;

public class DropShippingOrderDetailUpdateDao implements
		IDropShippingOrderDetailUpdateDao {
	@Inject
	private DropShippingOrderDetailMapper mapper;

	@Override
	public int batchInsert(List<DropShippingOrderDetail> list) {
		return mapper.batchInsert(list);
	}
}
