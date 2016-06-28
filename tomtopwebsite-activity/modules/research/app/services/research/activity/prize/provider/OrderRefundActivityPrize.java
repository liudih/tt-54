package services.research.activity.prize.provider;

import org.apache.commons.lang3.RandomStringUtils;

import com.google.inject.Inject;

import play.Logger;
import services.base.message.MessageService;
import valueobjects.base.activity.ActivityContext;
import valueobjects.base.activity.ActivityStatus;
import valueobjects.base.activity.LotteryResult;
import valueobjects.base.activity.param.OrderRefundActivityParam;
import valueobjects.base.activity.param.PrizeActivityParam;
import valueobjects.base.activity.result.ActivityResult;
import valueobjects.base.activity.result.OrderRefundActivityResult;
import valueobjects.base.activity.rule.ActivityRuleResult;
import entity.activity.page.PagePrizeResult;

/**
 * 奖品类型-订单退款
 * 
 * @author Administrator
 *
 */
public class OrderRefundActivityPrize extends ActivityPrize {

	// Logger.ALogger logger = Logger.of(this.getClass());

	@Inject
	MessageService messageService;

	@Override
	public String getName() {
		return "order refund";
	}

	@Override
	public int getPriority() {
		return 30;
	}

	public String getCprizeValue(ActivityContext activityContext,
			ActivityRuleResult activityRuleResult, PrizeActivityParam param) {
		return "OR" + RandomStringUtils.randomNumeric(10);
	}

	@Override
	public ActivityResult onAddRecordSuccess(ActivityContext activityContext,
			ActivityRuleResult activityRuleResult, PrizeActivityParam param,
			PagePrizeResult result) {
		messageService
				.send(result.getCemail(), activityContext
						.getActivityComponentParam().getName(), result
						.getCprizevalue(),
						MessageService.MessageType.ORDERREFUND);
		OrderRefundActivityResult orar = new OrderRefundActivityResult(
				result.getCprizevalue(), ActivityStatus.SUCC);
		return orar;
	};

	@Override
	public Class<?> getParam() {
		return OrderRefundActivityParam.class;
	}
}
