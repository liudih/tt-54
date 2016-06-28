package valueobjects.base.activity.result;

import valueobjects.base.activity.ActivityStatus;

/**
 * 奖品类型-订单退款，返回结果
 * @author Administrator
 *
 */
public class OrderRefundActivityResult extends ActivityResult {

	/**
	 * 返回生成的8位订单退款唯一码
	 */
	String orderRefundCode;

	public OrderRefundActivityResult(String orderRefundCode, ActivityStatus activityStatus) {
		super(activityStatus);
		this.orderRefundCode = orderRefundCode;
	}

	public String getOrderRefundCode() {
		return orderRefundCode;
	}

	public void setOrderRefundCode(String orderRefundCode) {
		this.orderRefundCode = orderRefundCode;
	}
}
