package valueobjects.base.activity.rule;

import valueobjects.base.activity.param.PrizeLotteryTimesActivityParam;

public class LimitLotteryActivityRuleResult extends ActivityRuleResult {

	@Override
	public Class<?> getPrizeParam() {
		return PrizeLotteryTimesActivityParam.class;
	}

	@Override
	public boolean checking(Object prizeParam) {
		return false;
	}

}
