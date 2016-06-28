package services.order;

import dto.order.OrderAlterHistory;

public interface IOrderAlterHistoryService {

	public abstract boolean insertOrderAlterHistory(
			OrderAlterHistory orderAlterHistory);

	public abstract OrderAlterHistory getOrderAlterHistoryById(Integer iid);

	public abstract OrderAlterHistory getEarliestByOrder(Integer orderId);

}