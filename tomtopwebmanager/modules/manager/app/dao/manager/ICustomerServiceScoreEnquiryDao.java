package dao.manager;

import java.util.Date;
import java.util.List;

import dao.IManagerEnquiryDao;
import dto.LivechatSessionScoreStatistics;
import entity.manager.CustomerServiceScore;

public interface ICustomerServiceScoreEnquiryDao extends IManagerEnquiryDao {
	List<CustomerServiceScore> getPage(int page, int pageSize);

	List<CustomerServiceScore> searchPage(String name, Integer typeID,
			int page, int pageSize);

	int count();

	int searchCount(String name, Integer typeID);
	
	List<LivechatSessionScoreStatistics> getScoreStatistics(Date beginDate,
			Date endDate, String dateFormat,String username);
}
