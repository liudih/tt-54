package valueobjects.base.activity.param;

import java.util.Date;

import extensions.activity.annotation.ParamInfo;
import extensions.activity.annotation.ParamType;

public class CouponPrizeActivityParam extends PrizeActivityParam {

	public CouponPrizeActivityParam() {
	}

	/**
	 * 每个多少积分
	 */
	@ParamInfo(desc = "coupon rule", type = ParamType.couponrule, priority = 1)
	private String couponRule;

	public CouponPrizeActivityParam(int number, Date beginDate, Date endDate,
			String couponRule) {
		super(number, beginDate, endDate);
		this.couponRule = couponRule;
	}

	public String getCouponRule() {
		return couponRule;
	}

	public void setCouponRule(String couponRule) {
		this.couponRule = couponRule;
	}
}
