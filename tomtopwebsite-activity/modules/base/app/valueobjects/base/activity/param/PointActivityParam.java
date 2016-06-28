package valueobjects.base.activity.param;

import extensions.activity.annotation.ParamInfo;

/**
 * 积分参数
 * 
 * @author fcl
 *
 */
public class PointActivityParam extends ActivityParam {

	@ParamInfo(desc = "points", priority = 1)
	private int points;

	public int getPoints() {
		return points;
	}

	public void setPoints(int points) {
		this.points = points;
	}

}
