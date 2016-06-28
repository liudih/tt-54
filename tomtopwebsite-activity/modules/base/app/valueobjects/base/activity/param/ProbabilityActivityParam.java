package valueobjects.base.activity.param;

import extensions.activity.annotation.ParamInfo;

/**
 * 概率抽奖参数
 * 
 * @author fcl
 *
 */
public class ProbabilityActivityParam extends ActivityParam {

	/**
	 * 基数
	 */
	@ParamInfo(desc = "Probability base", priority = 1)
	int probabilitybase;

	public int getProbabilitybase() {
		return probabilitybase;
	}

	public void setProbabilitybase(int probabilitybase) {
		this.probabilitybase = probabilitybase;
	}

}
