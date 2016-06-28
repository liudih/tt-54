package dao.order.impl;

import java.util.List;

import javax.inject.Inject;

import mapper.order.PreparatoryDetailMapper;
import dao.order.IPreparatoryDetailEnquiryDao;
import dto.order.PreparatoryDetail;

public class PreparatoryDetailEnquiryDao implements
		IPreparatoryDetailEnquiryDao {
	@Inject
	private PreparatoryDetailMapper mapper;

	@Override
	public List<PreparatoryDetail> getByOrderID(int orderID) {
		return mapper.getByOrderID(orderID);
	}

}
