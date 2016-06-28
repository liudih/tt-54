package dao.manager.impl;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import mapper.manager.CustomerServiceScheduleMapper;
import dao.manager.ICustomerServiceScheduleEnquiryDao;
import entity.manager.CustomerServiceSchedule;
import forms.CustomerServiceScheduleSearchForm;

public class CustomerServiceScheduleEnquiryDao implements
		ICustomerServiceScheduleEnquiryDao {
	@Inject
	CustomerServiceScheduleMapper mapper;

	@Override
	public List<CustomerServiceSchedule> getPage(int page, int pageSize) {
		return mapper.getPage(page, pageSize);
	}

	@Override
	public List<CustomerServiceSchedule> getByWeekOfYear(int weekOfYear) {
		return mapper.getByWeekOfYear(weekOfYear);
	}

	@Override
	public int getCount() {
		return mapper.getCount();
	}

	@Override
	public CustomerServiceSchedule getByID(int id) {
		return mapper.getByID(id);
	}

	@Override
	public List<CustomerServiceSchedule> searchPage(
			CustomerServiceScheduleSearchForm searchForm) {
		return mapper.searchPage(searchForm);
	}

	@Override
	public List<CustomerServiceSchedule> getUsers(Date date) {
		return mapper.getList(date);
	}

	@Override
	public int getCount(CustomerServiceScheduleSearchForm searchForm) {
		return mapper.searchCount(searchForm);
	}

	@Override
	public List<Integer> getAllScheduleUser() {
		return mapper.getAllScheduleUser();
	}

}
