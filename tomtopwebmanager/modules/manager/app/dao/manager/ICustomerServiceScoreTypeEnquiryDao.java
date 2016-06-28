package dao.manager;

import java.util.List;

import dao.IManagerEnquiryDao;
import entity.manager.CustomerServiceScoreType;

public interface ICustomerServiceScoreTypeEnquiryDao extends IManagerEnquiryDao {
	CustomerServiceScoreType getByID(int id);

	List<CustomerServiceScoreType> getPage(int page, int pageSize);

	int count();

	List<CustomerServiceScoreType> getAll();
	
	List<CustomerServiceScoreType> getTypeByLanguageId(int languageid);
	
}
