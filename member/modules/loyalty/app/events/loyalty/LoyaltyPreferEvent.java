package events.loyalty;

import valueobjects.loyalty.LoyaltyPrefer;

public class LoyaltyPreferEvent extends LoyaltyPrefer{

	/**
	 * 订单优惠事件,订单生成成功后应用此事件记录所有优惠,coupon or 推广码 or 积分
	 */
	private static final long serialVersionUID = 1L;
	private String orderNum;
	private Integer orderId;
	public Integer getOrderId() {
		return orderId;
	}
	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}
	public String getOrderNum() {
		return orderNum;
	}
	public void setOrderNum(String orderNum) {
		this.orderNum = orderNum;
	}
	

}
