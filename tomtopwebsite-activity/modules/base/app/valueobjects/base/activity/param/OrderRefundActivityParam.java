package valueobjects.base.activity.param;

import extensions.activity.annotation.ParamInfo;

/**
 * 订单退款参数
 * 
 * @author Administrator
 *
 */
public class OrderRefundActivityParam extends PrizeActivityParam {

	/**
	 * 退款的百分比
	 */
	@ParamInfo(desc = "order refund percent", priority = 4)
	private float percent;

	public float getPercent() {
		return percent;
	}

	public void setPercent(float percent) {
		this.percent = percent;
	}
}
