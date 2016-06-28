package valueobjects.base.activity.param;

import java.util.Date;

import extensions.activity.annotation.ParamInfo;

public class PointPrizeActivityParam extends PrizeActivityParam {

	/**
	 * 每个多少积分
	 */
	@ParamInfo(desc = "points", priority = 1)
	private int points;

	public PointPrizeActivityParam(int number, Date beginDate, Date endDate,
			int points) {
		super(number, beginDate, endDate);
		this.points = points;
	}

	public PointPrizeActivityParam() {
	}

	public void setPoints(int points) {
		this.points = points;
	}

	public int getPoints() {
		return this.points;
	}
}
