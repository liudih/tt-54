package valueobjects.base.activity.param;

import extensions.activity.annotation.ParamInfo;

/**
 * 次数参数
 * 
 * @author fcl
 *
 */
public class TimesActivityParam extends ActivityParam {

	@ParamInfo(desc = "times", priority = 1)
	private int times;

	public int getTimes() {
		return times;
	}

	public void setPoints(int times) {
		this.times = times;
	}

}
