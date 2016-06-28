package com.rabbit.services.iservice.order;


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

	
	public abstract String getOrderStatusNameById(Integer orderStatusId);

	public abstract boolean changeOrdeStatus(String orderId, String statusName);
	
	public abstract boolean changeOrdeStatus(Integer orderId, String statusName);
	Integer getIdByName(String name);

	boolean changeOrdeStatus(Integer orderId, String statusName, String email,
			String srcStatusName);

	boolean changeOrdeStatus(Integer orderId, Integer statusId, String email,
			Integer srcStatusId);

	boolean checkOrderStatus(Integer orderId, Integer statusId);
}