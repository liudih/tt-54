package dao.manager;

import java.util.Date;
import java.util.List;

import dao.IManagerEnquiryDao;
import entity.manager.CustomerServiceSchedule;
import forms.CustomerServiceScheduleSearchForm;

public interface ICustomerServiceScheduleEnquiryDao extends IManagerEnquiryDao {
	List<CustomerServiceSchedule> getPage(int page, int pageSize);

	List<CustomerServiceSchedule> getByWeekOfYear(int weekOfYear);

	int getCount();

	CustomerServiceSchedule getByID(int id);

	List<CustomerServiceSchedule> searchPage(
			CustomerServiceScheduleSearchForm searchForm);

	List<CustomerServiceSchedule> getUsers(Date date);

	int getCount(CustomerServiceScheduleSearchForm searchForm);
	
	List<Integer> getAllScheduleUser();

}
