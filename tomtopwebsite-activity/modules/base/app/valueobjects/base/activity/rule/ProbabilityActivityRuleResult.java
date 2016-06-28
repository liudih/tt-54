package valueobjects.base.activity.rule;

import play.Logger;
import valueobjects.base.activity.param.PercentActivityParam;

/**
 * 概率结果
 * 
 * @author fcl
 *
 */
public class ProbabilityActivityRuleResult extends ActivityRuleResult {

	double percentage;

	public ProbabilityActivityRuleResult() {
		super();
	}

	public ProbabilityActivityRuleResult(boolean passed, double percentage) {
		super(passed);
		this.percentage = percentage;
	}

	public double getPercentage() {
		return percentage;
	}

	@Override
	public boolean checking(Object obj) {
		if (obj != null && this.passed && obj instanceof PercentActivityParam) {
			PercentActivityParam pp = (PercentActivityParam) obj;
			return pp.getPercent() > this.percentage;
		}
		return false;
	}

	@Override
	public Class<?> getPrizeParam() {
		return PercentActivityParam.class;
	}
}
