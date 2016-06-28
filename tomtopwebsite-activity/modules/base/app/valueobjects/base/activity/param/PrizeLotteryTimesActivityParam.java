package valueobjects.base.activity.param;

import extensions.activity.annotation.ParamInfo;

public class PrizeLotteryTimesActivityParam extends ActivityParam {

	@ParamInfo(desc = "prizeLotteryTimes", priority = 1)
	private int prizeLotteryTimes;

	public int getPrizeLotteryTimes() {
		return prizeLotteryTimes;
	}

	public void setPrizeLotteryTimes(int prizeLotteryTimes) {
		this.prizeLotteryTimes = prizeLotteryTimes;
	}

}
