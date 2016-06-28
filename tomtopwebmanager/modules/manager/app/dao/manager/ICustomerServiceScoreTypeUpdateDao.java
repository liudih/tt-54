package dao.manager;

import dao.IManagerUpdateDao;
import entity.manager.CustomerServiceScoreType;

public interface ICustomerServiceScoreTypeUpdateDao extends IManagerUpdateDao {
	int insert(CustomerServiceScoreType type);

	int update(CustomerServiceScoreType type);

	int deleteByID(int id);
}
