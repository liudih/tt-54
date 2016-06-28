package dao.manager.impl;

import java.util.List;

import javax.inject.Inject;

import mapper.manager.CustomerServiceScoreTypeMapper;
import dao.manager.ICustomerServiceScoreTypeEnquiryDao;
import entity.manager.CustomerServiceScoreType;

public class CustomerServiceScoreTypeEnquiryDao implements
		ICustomerServiceScoreTypeEnquiryDao {
	@Inject
	CustomerServiceScoreTypeMapper mapper;

	@Override
	public CustomerServiceScoreType getByID(int id) {
		return mapper.getByID(id);
	}

	@Override
	public List<CustomerServiceScoreType> getPage(int page, int pageSize) {
		return mapper.getPage(page, pageSize);
	}

	@Override
	public int count() {
		return mapper.count();
	}

	@Override
	public List<CustomerServiceScoreType> getAll() {
		return mapper.getAll();
	}

	@Override
	public List<CustomerServiceScoreType> getTypeByLanguageId(int languageid) {
		return mapper.getScoreTypeByLanguageId(languageid);
	}

}
