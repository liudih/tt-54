package services.order;

import javax.inject.Inject;

import dao.order.IOrderAlterHistoryEnquiryDao;
import dao.order.IOrderAlterHistoryUpdateDao;
import dto.order.OrderAlterHistory;

public class OrderAlterHistoryService implements IOrderAlterHistoryService {
	@Inject
	IOrderAlterHistoryEnquiryDao orderAlterHistoryEnquiryDao;

	@Inject
	IOrderAlterHistoryUpdateDao orderAlterHistoryUpdateDao;

	/* (non-Javadoc)
	 * @see services.order.IOrderAlterHistoryService#insertOrderAlterHistory(dto.order.OrderAlterHistory)
	 */
	@Override
	public boolean insertOrderAlterHistory(OrderAlterHistory orderAlterHistory) {
		return orderAlterHistoryUpdateDao.insert(orderAlterHistory) > 0;
	}

	/* (non-Javadoc)
	 * @see services.order.IOrderAlterHistoryService#getOrderAlterHistoryById(java.lang.Integer)
	 */
	@Override
	public OrderAlterHistory getOrderAlterHistoryById(Integer iid) {
		return orderAlterHistoryEnquiryDao.getOrderAlterHistoryById(iid);
	}

	/* (non-Javadoc)
	 * @see services.order.IOrderAlterHistoryService#getEarliestByOrder(java.lang.Integer)
	 */
	@Override
	public OrderAlterHistory getEarliestByOrder(Integer orderId) {
		return orderAlterHistoryEnquiryDao.getEarliestByOrder(orderId);
	}
}
