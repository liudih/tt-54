package valueobjects.base.activity;

public class LotteryResult {
	//中奖等级
	private int lotteryLevel;
	//中奖描述
	private String desc;

	public LotteryResult() {
		super();
	}

	public LotteryResult(int lotteryLevel, String desc) {
		super();
		this.lotteryLevel = lotteryLevel;
		this.desc = desc;
	}

	public int getLotteryLevel() {
		return lotteryLevel;
	}

	public void setLotteryLevel(int lotteryLevel) {
		this.lotteryLevel = lotteryLevel;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

}
