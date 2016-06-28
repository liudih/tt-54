package valueobjects.base.activity.param;

import extensions.activity.annotation.ParamInfo;

public class LimitLotteryActivityParam extends ActivityParam {

	@ParamInfo(desc = "joinTimes", priority = 1)
	private int joinTimes;
	@ParamInfo(desc = "lotteryTimes", priority = 2)
	private int lotteryTimes;

	public int getJoinTimes() {
		return joinTimes;
	}

	public void setJoinTimes(int joinTimes) {
		this.joinTimes = joinTimes;
	}

	public int getLotteryTimes() {
		return lotteryTimes;
	}

	public void setLotteryTimes(int lotteryTimes) {
		this.lotteryTimes = lotteryTimes;
	}

}
