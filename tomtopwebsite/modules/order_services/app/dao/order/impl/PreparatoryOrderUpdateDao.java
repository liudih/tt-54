package dao.order.impl;

import java.util.List;

import javax.inject.Inject;

import mapper.order.PreparatoryOrderMapper;
import dao.order.IPreparatoryOrderUpdateDao;
import dto.order.PreparatoryOrder;

public class PreparatoryOrderUpdateDao implements IPreparatoryOrderUpdateDao {
	@Inject
	private PreparatoryOrderMapper mapper;

	@Override
	public int insert(PreparatoryOrder order) {
		return mapper.insert(order);
	}

	@Override
	public int updateNoByIDs(List<Integer> ids, int no) {
		return mapper.updateNoByIDs(ids, no);
	}

}
