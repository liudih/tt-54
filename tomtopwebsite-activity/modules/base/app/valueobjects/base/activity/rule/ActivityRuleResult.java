package valueobjects.base.activity.rule;

import valueobjects.base.activity.ActivityComponentParam;

public abstract class ActivityRuleResult {

	boolean passed;

	private ActivityComponentParam acp;

	public ActivityRuleResult() {
		super();
	}

	public ActivityRuleResult(boolean passed) {
		super();
		this.passed = passed;
	}

	public boolean isPassed() {
		return passed;
	}

	public void setPassed(boolean passed) {
		this.passed = passed;
	}

	/**
	 * 结果 参数;
	 */
	public abstract Class<?> getPrizeParam();

	/**
	 * 与配置结果比较
	 * 
	 * @param prizeParam
	 * @return
	 */
	public abstract boolean checking(Object prizeParam);

	public ActivityComponentParam getAcp() {
		return acp;
	}

	public void setAcp(ActivityComponentParam acp) {
		this.acp = acp;
	}

}
