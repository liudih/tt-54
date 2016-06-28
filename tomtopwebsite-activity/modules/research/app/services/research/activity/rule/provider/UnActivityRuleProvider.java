package services.research.activity.rule.provider;

import valueobjects.base.activity.ActivityComponentParam;
import valueobjects.base.activity.ActivityContext;
import valueobjects.base.activity.param.ActivityParam;
import valueobjects.base.activity.rule.ActivityRuleResult;
import valueobjects.base.activity.rule.UnActivityRuleResult;

import java.util.List;
import java.util.Map;

import extensions.activity.IActivityPrizeProvider;
import extensions.activity.IActivityRuleProvider;

public class UnActivityRuleProvider implements IActivityRuleProvider {

	@Override
	public String getName() {
		return "all";
	}

	@Override
	public int getPriority() {
		return 0;
	}

	@Override
	public Class<?> getParam() {
		return ActivityParam.class;
	}

	@Override
	public Class<?> getPrizeParam() {
		return new UnActivityRuleResult(true).getPrizeParam();
	}

	@Override
	public ActivityRuleResult execute(ActivityContext activityContext, List<ActivityComponentParam> prizesParams,
			Map<String, IActivityPrizeProvider> pmap) {
		ActivityRuleResult arr = new UnActivityRuleResult(true);
		arr.setAcp(new ActivityComponentParam());
		return arr;
	}

}
