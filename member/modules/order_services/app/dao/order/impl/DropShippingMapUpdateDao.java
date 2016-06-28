package dao.order.impl;

import java.util.List;

import javax.inject.Inject;

import mapper.order.DropShippingMapMapper;
import dao.order.IDropShippingMapUpdateDao;
import dto.order.dropShipping.DropShippingMap;

public class DropShippingMapUpdateDao implements IDropShippingMapUpdateDao {
	@Inject
	private DropShippingMapMapper mapper;

	@Override
	public int batchInsert(List<DropShippingMap> list) {
		return mapper.batchInsert(list);
	}

	@Override
	public int updateOrderNumber(String orderNumber, int dsOrderID) {
		return mapper.updateOrderID(orderNumber, dsOrderID);
	}

}
