package dao.manager;

import dao.IManagerUpdateDao;
import entity.manager.CustomerServiceScore;

public interface ICustomerServiceScoreUpdateDao extends IManagerUpdateDao {

	public int save(CustomerServiceScore csScore);
}
