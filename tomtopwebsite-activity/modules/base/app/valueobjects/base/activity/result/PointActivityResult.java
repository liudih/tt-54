package valueobjects.base.activity.result;

import valueobjects.base.activity.ActivityStatus;

public class PointActivityResult extends ActivityResult {

	int point;

	public PointActivityResult(int point, ActivityStatus activityStatus) {
		super(activityStatus);
		this.point = point;
	}

	public int getCount() {
		return point;
	}
}
