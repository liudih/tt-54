package dao.manager.impl;

import javax.inject.Inject;

import mapper.manager.CustomerServiceScheduleMapper;
import dao.manager.ICustomerServiceScheduleUpdateDao;
import entity.manager.CustomerServiceSchedule;

public class CustomerServiceScheduleUpdateDao implements
		ICustomerServiceScheduleUpdateDao {
	@Inject
	CustomerServiceScheduleMapper mapper;

	@Override
	public int insert(CustomerServiceSchedule schedule) {
		return mapper.insert(schedule);
	}

	@Override
	public int delete(int id) {
		return mapper.delete(id);
	}

}
