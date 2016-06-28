package dao.manager.impl;

import javax.inject.Inject;

import mapper.manager.CustomerServiceScoreMapper;
import mapper.manager.CustomerServiceScoreTypeMapper;
import dao.manager.ICustomerServiceScoreUpdateDao;
import entity.manager.CustomerServiceScore;

public class CustomerServiceScoreUpdateDao implements
		ICustomerServiceScoreUpdateDao {

	@Inject
	CustomerServiceScoreMapper mapper;

	@Override
	public int save(CustomerServiceScore csScore) {
		return mapper.insert(csScore);
	}

}
