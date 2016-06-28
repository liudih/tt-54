package dao.manager;

import dao.IManagerUpdateDao;
import entity.manager.CustomerServiceSchedule;

public interface ICustomerServiceScheduleUpdateDao extends IManagerUpdateDao {
	int insert(CustomerServiceSchedule schedule);

	int delete(int id);
}
