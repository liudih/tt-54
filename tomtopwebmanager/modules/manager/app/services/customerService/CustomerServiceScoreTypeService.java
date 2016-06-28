package services.customerService;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import javax.inject.Inject;

import org.apache.commons.beanutils.BeanUtils;

import play.Logger;
import valueobjects.base.Page;
import dao.manager.ICustomerServiceScoreTypeEnquiryDao;
import dao.manager.ICustomerServiceScoreTypeUpdateDao;
import entity.manager.CustomerServiceScoreType;
import forms.CustomerServiceScoreTypeForm;

public class CustomerServiceScoreTypeService {
	@Inject
	ICustomerServiceScoreTypeEnquiryDao enquiryDao;
	@Inject
	ICustomerServiceScoreTypeUpdateDao updateDao;

	private int pageSize = 15;

	public CustomerServiceScoreType getByID(int id) {
		return enquiryDao.getByID(id);
	}

	public Page<CustomerServiceScoreType> getPage(int page) {
		List<CustomerServiceScoreType> list = enquiryDao
				.getPage(page, pageSize);
		int count = count();
		return new Page<CustomerServiceScoreType>(list, count, page, pageSize);
	}

	public int count() {
		return enquiryDao.count();
	}

	public boolean insert(CustomerServiceScoreType type) {
		int i = updateDao.insert(type);
		return 1 == i ? true : false;
	}

	public boolean update(CustomerServiceScoreType type) {
		int i = updateDao.update(type);
		return 1 == i ? true : false;
	}

	public boolean deleteByID(int id) {
		int i = updateDao.deleteByID(id);
		return 1 == i ? true : false;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public boolean insert(CustomerServiceScoreTypeForm typeForm) {
		CustomerServiceScoreType type = new CustomerServiceScoreType();
		try {
			BeanUtils.copyProperties(type, typeForm);
			return insert(type);
		} catch (IllegalAccessException e) {
			Logger.error("BeanUtils.copyProperties: ", e);
			return false;
		} catch (InvocationTargetException e) {
			Logger.error("BeanUtils.copyProperties: ", e);
			return false;
		}
	}

	public boolean update(CustomerServiceScoreTypeForm typeForm) {
		CustomerServiceScoreType type = new CustomerServiceScoreType();
		try {
			BeanUtils.copyProperties(type, typeForm);
			return update(type);
		} catch (IllegalAccessException e) {
			Logger.error("BeanUtils.copyProperties: ", e);
			return false;
		} catch (InvocationTargetException e) {
			Logger.error("BeanUtils.copyProperties: ", e);
			return false;
		}
	}

	public List<CustomerServiceScoreType> getAll() {
		return enquiryDao.getAll();
	}

}
