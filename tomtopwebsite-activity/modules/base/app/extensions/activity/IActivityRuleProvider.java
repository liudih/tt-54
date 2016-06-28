package extensions.activity;

import java.util.List;
import java.util.Map;

import valueobjects.base.activity.ActivityComponentParam;
import valueobjects.base.activity.ActivityContext;
import valueobjects.base.activity.param.ActivityParam;
import valueobjects.base.activity.rule.ActivityRuleResult;

/**
 * 活动规则
 * @author fcl
 *
 */
public interface IActivityRuleProvider {

	/**
	 * 规则名称，注明清楚规则的主要作用
	 * @return
	 */
	public String getName();
	/**
	 * 排序
	 * @return
	 */
	public int getPriority();
	/**
	 * 规则参数，通过这可以知道这个规则是需要什么参数;
	 * @return
	 */
	public Class<?> getParam();
	
	/**
	 * 定义使用这个规则  需要配置的結果參數
	 * @return
	 */
	abstract Class<?> getPrizeParam();
	
	public ActivityRuleResult execute(ActivityContext activityContext, List<ActivityComponentParam> prizesParams, Map<String, IActivityPrizeProvider> pmap);
	
}
