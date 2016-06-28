package dao.order.impl;

import javax.inject.Inject;

import mapper.order.DropShippingMapper;
import dao.order.IDropShippingEnquiryDao;
import dto.order.dropShipping.DropShipping;

public class DropShippingEnquiryDao implements IDropShippingEnquiryDao {
	@Inject
	DropShippingMapper mapper;

	@Override
	public DropShipping getByID(int id) {
		return mapper.getByID(id);
	}

	@Override
	public DropShipping getByDropShippingID(String id) {
		return mapper.getByDropShippingID(id);
	}

	@Override
	public Double getSumPriceByEmail(String email, int site) {
		return mapper.getSumPriceByEmail(email, site);
	}

}