package dao.order.impl;

import javax.inject.Inject;

import mapper.order.TotalOrderMapper;
import dao.order.ITotalOrderDao;
import dto.order.TotalOrder;

public class TotalOrderDao implements ITotalOrderDao {
	@Inject
	private TotalOrderMapper mapper;

	@Override
	public TotalOrder getByID(Integer id) {
		return mapper.getByID(id);
	}

	@Override
	public TotalOrder getByCID(String cid) {
		return mapper.getByCID(cid);
	}

	@Override
	public int insert(String cid) {
		return mapper.insert(cid);
	}

}
