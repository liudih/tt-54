package  com.rabbit.services.serviceImp.order;


import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rabbit.conf.ordermapper.order.OrderMapper;
import com.rabbit.conf.ordermapper.order.OrderStatusMapper;
import com.rabbit.conf.ordermapper.order.StatusHistoryMapper;
import com.rabbit.services.iservice.order.IOrderStatusService;
@Service
public class OrderStatusService implements IOrderStatusService {
	
	private static Logger log=Logger.getLogger(OrderStatusService.class.getName());
	
	@Autowired
	OrderStatusMapper statusMapper;

	@Autowired
	OrderMapper orderMapper;
	@Autowired
	StatusHistoryMapper historyMapper;
	@Override
	public boolean changeOrdeStatus(String orderId, String statusName) {
		Integer iid = orderMapper.getOrderIdByOrderNumber(orderId);
		Integer statusID = getIdByName(statusName);
		if (iid == null || statusID == null) {
			return false;
		}
		return changeOrdeStatus(iid, statusID, null, null);
	}
	@Override
	/*@CacheResult("cache.order.status")*/
	public Integer getIdByName(String name) {
		Integer id = statusMapper.getIdByName(name);
		if (null == id) {
			log.error("Not found the status: {}"+name);
		}
		return id;
	}

	@Override
	public boolean changeOrdeStatus(Integer orderId, String statusName,
			String email, String srcStatusName) {
		Integer statusId = getIdByName(statusName);
		Integer srcStatusid = getIdByName(srcStatusName);
		if (null == statusId || null == srcStatusid) {
			return false;
		}
		return changeOrdeStatus(orderId, statusId, email, srcStatusid);
	}
	@Override
	public boolean changeOrdeStatus(Integer orderId, Integer statusId,
			String email, Integer srcStatusId) {
		if (checkOrderStatus(orderId, statusId)) {
			return true;
		}
		int i = orderMapper.updateStatus(orderId, statusId, email, srcStatusId);
		if (1 == i) {
			int f = historyMapper.update(orderId, statusId);
			if (0 == f) {
				historyMapper.insert(orderId, statusId);
			}
			return true;
		}
		return false;
	}
	@Override
	public boolean checkOrderStatus(Integer orderId, Integer statusId) {
		int i = orderMapper.checkOrderStatus(orderId, statusId);
		if (i > 0) {
			return true;
		}
		return false;
	}
	@Override
	public boolean changeOrdeStatus(Integer orderId, String statusName) {
		Integer statusId = getIdByName(statusName);
		if (null == statusId) {
			return false;
		}
		return changeOrdeStatus(orderId, statusId, null, null);
	}
	/*@Autowired
	StatusHistoryMapper historyMapper;*/

/*	 (non-Javadoc)
	 * @see services.order.IOrderStatusService#getIdByName(java.lang.String)
	 

	 (non-Javadoc)
	 * @see services.order.IOrderStatusService#getAll()
	 
	@Override
	@CacheResult("cache.order.status")
	public List<OrderStatus> getAll() {
		return statusMapper.getAll();
	}

	 (non-Javadoc)
	 * @see services.order.IOrderStatusService#getNameMap()
	 
	@Override
	@CacheResult("cache.order.status")
	public Map<String, OrderStatus> getNameMap() {
		List<OrderStatus> list = getAll();
		Map<String, OrderStatus> map = Maps
				.uniqueIndex(list, o -> o.getCname());
		return map;
	}

	 (non-Javadoc)
	 * @see services.order.IOrderStatusService#getIdMap()
	 
	@Override
	@CacheResult("cache.order.status")
	public Map<Integer, OrderStatus> getIdMap() {
		List<OrderStatus> list = getAll();
		Map<Integer, OrderStatus> map = Maps.uniqueIndex(list, o -> o.getIid());
		return map;
	}

	public static IOrderStatusService getInstance() {
		return InjectorInstance.getInjector().getInstance(
				OrderStatusService.class);
	}

	 (non-Javadoc)
	 * @see services.order.IOrderStatusService#changeOrdeStatusValidEmailAndStatus(java.lang.Integer, java.lang.String, java.lang.String, java.lang.String)
	 
	@Override
	public boolean changeOrdeStatusValidEmailAndStatus(Integer orderId,
			String statusName, String email, String srcStatusName) {
		if (null == email) {
			return false;
		}
		return changeOrdeStatus(orderId, statusName, email, srcStatusName);
	}

	// need recode
	 (non-Javadoc)
	 * @see services.order.IOrderStatusService#changeOrdeStatus(java.lang.Integer, java.lang.String, java.lang.String, java.lang.String)
	 

	 (non-Javadoc)
	 * @see services.order.IOrderStatusService#changeOrdeStatus(java.lang.String, java.lang.String)
	 

	 (non-Javadoc)
	 * @see services.order.IOrderStatusService#changeOrdeStatus(java.lang.Integer, java.lang.String)
	 

	 (non-Javadoc)
	 * @see services.order.IOrderStatusService#changeOrdeStatusInBackstage(java.lang.Integer, java.lang.Integer)
	 
	@Override
	public boolean changeOrdeStatusInBackstage(Integer orderId, Integer statusId) {
		Integer srcStatusid = orderMapper.getOrderStatusByOrderId(orderId);
		if (!changeStatusCheck(statusId, srcStatusid)) {
			return false;
		}
		return changeOrdeStatus(orderId, statusId, null, srcStatusid);
	}

	 (non-Javadoc)
	 * @see services.order.IOrderStatusService#changeOrdeStatus(java.lang.Integer, java.lang.Integer, java.lang.Integer)
	 
	@Override
	public boolean changeOrdeStatus(Integer orderId, Integer statusId,
			Integer srcStatusId) {
		return changeOrdeStatus(orderId, statusId, null, srcStatusId);
	}

	 (non-Javadoc)
	 * @see services.order.IOrderStatusService#changeOrdeStatus(java.lang.Integer, java.lang.Integer)
	 
	@Override
	public boolean changeOrdeStatus(Integer orderId, Integer statusId) {
		return changeOrdeStatus(orderId, statusId, null, null);
	}

	 (non-Javadoc)
	 * @see services.order.IOrderStatusService#changeOrdeStatus(java.lang.Integer, java.lang.Integer, java.lang.String, java.lang.Integer)
	 

	 (non-Javadoc)
	 * @see services.order.IOrderStatusService#changeStatusCheck(java.lang.Integer, java.lang.Integer)
	 
	@Override
	public boolean changeStatusCheck(Integer statusId, Integer srcStatusId) {
		OrderStatus srcStatus = getIdMap().get(srcStatusId);
		OrderStatus status = getIdMap().get(statusId);
		if (null == srcStatus) {
			return true;
		} else if (null == statusId) {
			return false;
		}
		if (srcStatus.getIorder() < 500
				& status.getIorder() > srcStatus.getIorder()) {
			return true;
		} else if (srcStatus.getIorder() >= 500
				&& status.getIorder().equals(srcStatus.getIorder() * 2)) {
			return true;
		}
		return false;
	}

	 (non-Javadoc)
	 * @see services.order.IOrderStatusService#getOrderHistory(java.lang.Integer)
	 
	@Override
	public List<OrderStatusHistory> getOrderHistory(Integer orderId) {
		return historyMapper.getHistories(orderId);
	}

	 (non-Javadoc)
	 * @see services.order.IOrderStatusService#getOrderHistoryMap(java.lang.Integer)
	 
	@Override
	public Map<String, OrderStatusHistory> getOrderHistoryMap(Integer orderId) {
		List<OrderStatusHistory> list = getOrderHistory(orderId);
		Map<Integer, OrderStatus> statusMap = getIdMap();
		Map<String, OrderStatusHistory> map = Maps.newHashMap();
		for (OrderStatusHistory osh : list) {
			map.put(statusMap.get(osh.getIstatus()).getCname(), osh);
		}
		return map;
	}

	 (non-Javadoc)
	 * @see services.order.IOrderStatusService#checkOrderStatus(java.lang.Integer, java.lang.Integer)
	 
*/
	/* (non-Javadoc)
	 * @see services.order.IOrderStatusService#getOrderStatusNameById(java.lang.Integer)
	 */
	@Override
	/*@CacheResult("cache.order.status")*/
	public String getOrderStatusNameById(Integer orderStatusId) {
		return statusMapper.getNameById(orderStatusId);
	}

	/* (non-Javadoc)
	 * @see services.order.IOrderStatusService#getIdForPayment()
	 
	@Override
	@CacheResult("cache.order.status")
	public List<Integer> getIdForPayment() {
		List<String> namelist = Lists.newArrayList();
		namelist.add(IOrderStatusService.PAYMENT_CONFIRMED);
		namelist.add(IOrderStatusService.PROCESSING);
		namelist.add(IOrderStatusService.DISPATCHED);
		namelist.add(IOrderStatusService.COMPLETED);
		List<OrderStatus> olist = statusMapper.getStatusForPayment(namelist);
		return Lists.transform(olist, o -> o.getIid());
	}

	@Override
	public boolean changeOrdeStatusAndTransactionInfo(Integer orderId, Integer statusId,
			String transactionId, String receiverAccount,String paymentId) {
		Logger.debug("order id:{}",orderId);
		Map<String,Object> paras = Maps.newHashMap();
		paras.put("id", orderId);
		paras.put("status", statusId);
		paras.put("transactionId", transactionId);
		paras.put("receiverAccount", receiverAccount);
		paras.put("paymentId", paymentId);
		
		int i = orderMapper.changeOrdeStatusAndTransactionInfo(paras);
		if (1 == i) {
			int f = historyMapper.update(orderId, statusId);
			if (0 == f) {
				historyMapper.insert(orderId, statusId);
			}
			return true;
		}
		return false;
	}

	@Override
	public boolean changeOrdePaymentStatus(String orderNum,Integer statusId) {
		if(orderNum == null || orderNum.length() == 0){
			throw new NullPointerException("orderNum is null");
		}
		Integer result = this.orderMapper.changeOrdePaymentStatus(orderNum, statusId);
		return result > 0 ? true : false;
	}*/
}
