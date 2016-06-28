package dao.order.impl;

import javax.inject.Inject;

import mapper.order.DropShippingMapper;
import dao.order.IDropShippingUpdateDao;
import dto.order.dropShipping.DropShipping;

public class DropShippingUpdateDao implements IDropShippingUpdateDao {
	@Inject
	DropShippingMapper mapper;

	@Override
	public int insert(DropShipping ds) {
		return mapper.insert(ds);
	}

	@Override
	public int updateByID(DropShipping ds) {
		return mapper.updateByID(ds);
	}

	@Override
	public int updateByDropShippingID(DropShipping ds) {
		return mapper.updateByDropShippingID(ds);
	}

	@Override
	public int setUsedByDropShippingID(String dropShippingID, boolean bused) {
		return mapper.setUsedByDropShippingID(dropShippingID, bused);
	}

}
