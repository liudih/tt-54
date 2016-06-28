package valueobjects.base.activity.result;

import valueobjects.base.activity.ActivityStatus;

public class VoteActivityResult extends ActivityResult {

	int pateItemid;
	int count;

	public VoteActivityResult(int pateItemid, int count,
			ActivityStatus activityStatus) {
		super(activityStatus);
		this.pateItemid = pateItemid;
		this.count = count;
	}

	public int getPateItemid() {
		return pateItemid;
	}

	public int getCount() {
		return count;
	}
}
