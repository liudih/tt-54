package dao.order;

import dao.IOrderEnquiryDao;
import dto.order.OrderAlterHistory;

public interface IOrderAlterHistoryEnquiryDao extends IOrderEnquiryDao {
	public OrderAlterHistory getOrderAlterHistoryById(Integer iid);

	public OrderAlterHistory getEarliestByOrder(Integer orderId);
}
