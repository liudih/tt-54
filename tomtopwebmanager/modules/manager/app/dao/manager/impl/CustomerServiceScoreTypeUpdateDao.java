package dao.manager.impl;

import javax.inject.Inject;

import mapper.manager.CustomerServiceScoreTypeMapper;
import dao.manager.ICustomerServiceScoreTypeUpdateDao;
import entity.manager.CustomerServiceScoreType;

public class CustomerServiceScoreTypeUpdateDao implements
		ICustomerServiceScoreTypeUpdateDao {
	@Inject
	CustomerServiceScoreTypeMapper mapper;

	@Override
	public int insert(CustomerServiceScoreType type) {
		return mapper.insert(type);
	}

	@Override
	public int update(CustomerServiceScoreType type) {
		return mapper.update(type);
	}

	@Override
	public int deleteByID(int id) {
		return mapper.deleteByID(id);
	}

}
