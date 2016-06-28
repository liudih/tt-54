package extensions.activity;

import java.util.List;

import valueobjects.base.activity.ActivityContext;
import valueobjects.base.activity.result.ActivityResult;
import valueobjects.base.activity.rule.ActivityRuleResult;

/**
 * 奖品结果處理
 * 
 * @author fcl
 *
 */
public interface IActivityPrizeProvider {

	public String getName();

	public int getPriority();
	
	public Class<?> getParam();

	public ActivityResult match(ActivityContext activityContext,
			ActivityRuleResult activityRuleResult);
}
