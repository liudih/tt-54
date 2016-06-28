package valueobjects.base.activity.rule;

import valueobjects.base.activity.param.ActivityParam;

/**
 * 概率结果
 * 
 * @author fcl
 *
 */
public class UnActivityRuleResult extends ActivityRuleResult {

	public UnActivityRuleResult(boolean passed) {
		super(passed);
	}

	@Override
	public boolean checking(Object obj) {
		return true;
	}

	@Override
	public Class<?> getPrizeParam() {
		return ActivityParam.class;
	}
}
