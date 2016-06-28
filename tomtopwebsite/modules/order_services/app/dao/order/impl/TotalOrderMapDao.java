package dao.order.impl;

import java.util.List;

import javax.inject.Inject;

import mapper.order.TotalOrderMapMapper;
import dao.order.ITotalOrderMapDao;
import dto.order.TotalOrderMap;

public class TotalOrderMapDao implements ITotalOrderMapDao {
	@Inject
	private TotalOrderMapMapper mapper;

	@Override
	public List<TotalOrderMap> getByTotalID(Integer totalID) {
		return mapper.getByTotalID(totalID);
	}

	@Override
	public TotalOrderMap getByOrderID(Integer orderID) {
		return mapper.getByOrderID(orderID);
	}

	@Override
	public int batchInsert(List<TotalOrderMap> list) {
		return mapper.batchInsert(list);
	}

}
