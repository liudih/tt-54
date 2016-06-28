package services.research.activity.prize.provider;

import play.Logger;
import services.base.FoundationService;
import services.base.message.MessageService;
import services.loyalty.coupon.ICouponService;
import valueobjects.base.activity.ActivityContext;
import valueobjects.base.activity.ActivityStatus;
import valueobjects.base.activity.LotteryResult;
import valueobjects.base.activity.param.CouponPrizeActivityParam;
import valueobjects.base.activity.param.PrizeActivityParam;
import valueobjects.base.activity.result.ActivityResult;
import valueobjects.base.activity.rule.ActivityRuleResult;
import entity.loyalty.Coupon;

import com.google.inject.Inject;

import entity.activity.page.PagePrizeResult;

public class CouponActivityPrize extends ActivityPrize {

	@Inject
	private ICouponService couponService;

	@Inject
	MessageService messageService;

	@Inject
	FoundationService foundationService;

	@Override
	public String getName() {
		return "coupon rule";
	}

	@Override
	public int getPriority() {
		return 0;
	}

	@Override
	public Class<?> getParam() {
		return CouponPrizeActivityParam.class;
	}

	@Override
	public String getCprizeValue(ActivityContext activityContext,
			ActivityRuleResult activityRuleResult, PrizeActivityParam param) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ActivityResult onAddRecordSuccess(ActivityContext activityContext,
			ActivityRuleResult activityRuleResult, PrizeActivityParam param,
			PagePrizeResult result) {
		try {
			int couponRule = Integer.valueOf(((CouponPrizeActivityParam) param)
					.getCouponRule());
			Coupon coupon = couponService.createCouponByRule(
					result.getCemail(), couponRule,
					foundationService.getWebContext());
			Logger.debug("website-->{}",foundationService.getWebContext());
			if (null != coupon) {
				messageService.send(result.getCemail(), activityContext
						.getActivityComponentParam().getName(), coupon
						.getCode(), MessageService.MessageType.COUPON);
				return new ActivityResult(ActivityStatus.SUCC);
			} else {
				Logger.error(
						"The draw record has been generated, but the send coupon fail,user:"
								+ result.getCemail() + ",prize id:"
								+ result.getIprizeid(), this.getClass());
			}
		} catch (Exception e) {
			Logger.error(
					"The draw record has been generated, but the send coupon fail,user:"
							+ result.getCemail() + ",prize id:"
							+ result.getIprizeid() + ",errormsg"
							+ e.getMessage(), this.getClass());
		}
		return new ActivityResult(ActivityStatus.SUCC);
	}
}
