package dao.order.impl;

import java.util.List;

import javax.inject.Inject;

import mapper.order.PreparatoryOrderMapper;
import dao.order.IPreparatoryOrderEnquiryDao;
import dto.order.PreparatoryOrder;

public class PreparatoryOrderEnquiryDao implements IPreparatoryOrderEnquiryDao {
	@Inject
	private PreparatoryOrderMapper mapper;

	@Override
	public PreparatoryOrder getByID(int id) {
		return mapper.getByID(id);
	}

	@Override
	public List<PreparatoryOrder> getByCartID(String cartID) {
		return mapper.getByCartID(cartID);
	}

}
