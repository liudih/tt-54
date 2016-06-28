package services.order;

import java.util.List;
import java.util.Map;

import dto.order.OrderStatus;
import dto.order.OrderStatusHistory;

public interface IOrderStatusService {

	/**
	 * 待付款
	 */
	public static final String PAYMENT_PENDING = "Payment Pending";
	/**
	 * 收款处理中
	 */
	public static final String PAYMENT_PROCESSING = "Payment Processing";
	/**
	 * 收款成功
	 */
	public static final String PAYMENT_CONFIRMED = "Payment Confirmed";
	/**
	 * 订单已取消
	 */
	public static final String ORDER_CANCELLED = "Order Cancelled";
	/**
	 * 订单正在处理中（从成功收款到到发货这段时间）
	 */
	public static final String PROCESSING = "Processing";
	/**
	 * 订单审核中，比如可能这个单的付款有些问题，我们暂时hold住，不发货
	 */
	public static final String ON_HOLD = "On Hold";
	/**
	 * 订单已发货
	 */
	public static final String DISPATCHED = "Dispatched";
	/**
	 * 订单已完成
	 */
	public static final String COMPLETED = "Completed";
	/**
	 * 已退款
	 */
	public static final String REFUNDED = "Refunded";

	public abstract Integer getIdByName(String name);

	public abstract List<OrderStatus> getAll();

	public abstract Map<String, OrderStatus> getNameMap();

	public abstract Map<Integer, OrderStatus> getIdMap();

	public abstract boolean changeOrdeStatusValidEmailAndStatus(
			Integer orderId, String statusName, String email,
			String srcStatusName);

	// need recode
	public abstract boolean changeOrdeStatus(Integer orderId,
			String statusName, String email, String srcStatusName);

	public abstract boolean changeOrdeStatus(String orderId, String statusName);

	public abstract boolean changeOrdeStatus(Integer orderId, String statusName);

	public abstract boolean changeOrdeStatusInBackstage(Integer orderId,
			Integer statusId);

	public abstract boolean changeOrdeStatus(Integer orderId, Integer statusId,
			Integer srcStatusId);

	public abstract boolean changeOrdeStatus(Integer orderId, Integer statusId);

	public abstract boolean changeOrdeStatus(Integer orderId, Integer statusId,
			String email, Integer srcStatusId);

	public abstract boolean changeStatusCheck(Integer statusId,
			Integer srcStatusId);

	public abstract List<OrderStatusHistory> getOrderHistory(Integer orderId);

	public abstract Map<String, OrderStatusHistory> getOrderHistoryMap(
			Integer orderId);

	public abstract boolean checkOrderStatus(Integer orderId, Integer statusId);

	public abstract String getOrderStatusNameById(Integer orderStatusId);

	/**
	 * 返回 已付款的订单状态
	 * 
	 * @return
	 */
	public abstract List<Integer> getIdForPayment();

	/**
	 * lijun
	 * 
	 * @param orderId
	 * @param statusId
	 * @param transactionId
	 * @param receiverAccount
	 * @return
	 */
	public boolean changeOrdeStatusAndTransactionInfo(Integer orderId,
			Integer statusId, String transactionId, String receiverAccount,
			String paymentId);

	/**
	 * 修改订单的支付状态
	 * 
	 * @author lijun
	 * @param orderNum
	 * @param statusId
	 * @return
	 */
	public boolean changeOrdePaymentStatus(String orderNum, Integer statusId);

}