package valueobjects.base.activity;

import java.util.Date;

/**
 * 活动参数
 * 
 * @author Administrator
 *
 */
public class ActivityContext {

	private String activityType;
	private int activityPageId;
	private int activityPageItemId;
	private int websiteId;
	private Date endDate;
	private Date beginDate;

	/**
	 * 类参数;
	 */
	ActivityComponentParam activityComponentParam;

	public ActivityContext(String activityType, int activityPageId,
			int activityPageItemId, int websiteId, Date endDate,
			Date beginDate, ActivityComponentParam activityComponentParam) {
		super();
		this.activityType = activityType;
		this.activityPageId = activityPageId;
		this.activityPageItemId = activityPageItemId;
		this.websiteId = websiteId;
		this.endDate = endDate;
		this.beginDate = beginDate;
		this.activityComponentParam = activityComponentParam;
	}

	public void changeComponentParam(
			ActivityComponentParam activityComponentParam) {
		this.activityComponentParam = activityComponentParam;
	}

	public String getActivityType() {
		return activityType;
	}

	public ActivityComponentParam getActivityComponentParam() {
		return activityComponentParam;
	}

	public int getActivityPageId() {
		return activityPageId;
	}

	public int getActivityPageItemId() {
		return this.activityPageItemId;
	}

	public int getWebsiteId() {
		return websiteId;
	}

	public Date getEndDate() {
		return endDate;
	}

	public Date getBeginDate() {
		return beginDate;
	}

}
