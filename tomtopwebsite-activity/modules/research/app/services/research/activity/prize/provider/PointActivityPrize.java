package services.research.activity.prize.provider;

import play.Logger;
import services.loyalty.IPointsService;
import valueobjects.base.activity.ActivityContext;
import valueobjects.base.activity.ActivityStatus;
import valueobjects.base.activity.LotteryResult;
import valueobjects.base.activity.param.PointPrizeActivityParam;
import valueobjects.base.activity.param.PrizeActivityParam;
import valueobjects.base.activity.result.ActivityResult;
import valueobjects.base.activity.result.OrderRefundActivityResult;
import valueobjects.base.activity.result.PointActivityResult;
import valueobjects.base.activity.rule.ActivityRuleResult;

import com.google.inject.Inject;

import entity.activity.page.PagePrizeResult;

/**
 * 送积分
 * 
 * @author Administrator
 *
 */
public class PointActivityPrize extends ActivityPrize {

	@Inject
	private IPointsService pointsService;

	@Override
	public String getName() {
		return "points-prize";
	}

	@Override
	public int getPriority() {
		return 20;
	}

	@Override
	public String getCprizeValue(ActivityContext activityContext,
			ActivityRuleResult activityRuleResult, PrizeActivityParam param) {
		return String.valueOf(((PointPrizeActivityParam) param).getPoints());
	}

	@Override
	public ActivityResult onAddRecordSuccess(ActivityContext activityContext,
			ActivityRuleResult activityRuleResult, PrizeActivityParam param,
			PagePrizeResult result) {
		try {
			if (!pointsService.grantPoints(result.getCemail(),
					result.getIwebsiteid(),
					((PointPrizeActivityParam) param).getPoints(), "luckdraw",
					"luckdraw", 1, "luckdraw")) {
				Logger.error(
						"The draw record has been generated, but the send points fail,user:"
								+ result.getCemail() + ",prize id:"
								+ result.getIprizeid(), this.getClass());
			}
		} catch (Exception e) {
			// TODO: handle exception
			Logger.error(
					"The draw record has been generated, but the send points fail,user:"
							+ result.getCemail() + ",prize id:"
							+ result.getIprizeid() + ",errormsg:"
							+ e.getMessage(), this.getClass());
		}
		OrderRefundActivityResult orar = new OrderRefundActivityResult(result.getCprizevalue(),
				ActivityStatus.SUCC);
		orar.setLotteryResult(new LotteryResult(1,"point-"+result.getCprizevalue()));
		return orar;
	}

	@Override
	public Class<?> getParam() {
		return PointPrizeActivityParam.class;
	}
}
