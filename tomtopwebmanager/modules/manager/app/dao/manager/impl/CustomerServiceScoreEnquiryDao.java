package dao.manager.impl;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import mapper.manager.CustomerServiceScoreMapper;
import dao.manager.ICustomerServiceScoreEnquiryDao;
import dto.LivechatSessionScoreStatistics;
import entity.manager.CustomerServiceScore;

public class CustomerServiceScoreEnquiryDao implements
		ICustomerServiceScoreEnquiryDao {
	@Inject
	CustomerServiceScoreMapper mapper;

	@Override
	public List<CustomerServiceScore> getPage(int page, int pageSize) {
		return mapper.getPage(page, pageSize);
	}

	@Override
	public List<CustomerServiceScore> searchPage(String name, Integer typeID,
			int page, int pageSize) {
		return mapper.searchPage(name, typeID, page, pageSize);
	}

	@Override
	public int count() {
		return mapper.count();
	}

	@Override
	public int searchCount(String name, Integer typeID) {
		return mapper.searchCount(name, typeID);
	}

	@Override
	public List<LivechatSessionScoreStatistics> getScoreStatistics(
			Date beginDate, Date endDate, String dateFormat, String username) {
		return mapper.getScoreStatistics(beginDate, endDate, dateFormat,
				username);
	}

}
