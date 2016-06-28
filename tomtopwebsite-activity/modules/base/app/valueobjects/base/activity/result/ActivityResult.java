package valueobjects.base.activity.result;

import valueobjects.base.activity.ActivityStatus;
import valueobjects.base.activity.LotteryResult;
import valueobjects.base.activity.param.JoinActivityParam;

public class ActivityResult {

	private ActivityStatus fixedStatus;

	private JoinActivityParam joinParam;
	
	private LotteryResult lotteryResult;

	public ActivityResult() {
		super();
	}

	public ActivityResult(ActivityStatus fixedStatus) {
		super();
		this.fixedStatus = fixedStatus;
	}

	public ActivityResult(ActivityStatus fixedStatus, JoinActivityParam joinParam) {
		super();
		this.fixedStatus = fixedStatus;
		this.joinParam = joinParam;
	}

	public ActivityStatus getFixedStatus() {
		return fixedStatus;
	}

	public void setFixedStatus(ActivityStatus fixedStatus) {
		this.fixedStatus = fixedStatus;
	}

	public JoinActivityParam getJoinParam() {
		return joinParam;
	}

	public void setJoinParam(JoinActivityParam joinParam) {
		this.joinParam = joinParam;
	}

	public LotteryResult getLotteryResult() {
		return lotteryResult;
	}

	public void setLotteryResult(LotteryResult lotteryResult) {
		this.lotteryResult = lotteryResult;
	}

}
