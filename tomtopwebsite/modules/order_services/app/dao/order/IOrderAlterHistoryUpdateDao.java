package dao.order;

import dao.IOrderUpdateDao;
import dto.order.OrderAlterHistory;

public interface IOrderAlterHistoryUpdateDao extends IOrderUpdateDao {
	public int insert(OrderAlterHistory record);
}
