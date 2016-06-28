package dao.order.impl;

import java.util.List;

import javax.inject.Inject;

import mapper.order.PreparatoryDetailMapper;
import dao.order.IPreparatoryDetailUpdateDao;
import dto.order.PreparatoryDetail;

public class PreparatoryDetailUpdateDao implements IPreparatoryDetailUpdateDao {
	@Inject
	private PreparatoryDetailMapper mapper;

	@Override
	public int batchInsert(List<PreparatoryDetail> list) {
		return mapper.batchInsert(list);
	}

}
