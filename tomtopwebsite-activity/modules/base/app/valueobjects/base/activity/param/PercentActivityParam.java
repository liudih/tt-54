package valueobjects.base.activity.param;

import extensions.activity.annotation.ParamInfo;

/**
 * 百分比参数
 * 
 * @author fcl
 *
 */
public class PercentActivityParam extends ActivityParam {

	/**
	 * 百分比
	 */
	@ParamInfo(desc = "percent", priority = 1)
	double percent;

	public double getPercent() {
		return percent;
	}

	public void setPercent(double percent) {
		this.percent = percent;
	}

}
