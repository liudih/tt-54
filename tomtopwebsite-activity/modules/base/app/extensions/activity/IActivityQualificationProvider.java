package extensions.activity;

import valueobjects.base.activity.ActivityContext;
import valueobjects.base.activity.ActivityStatus;
import valueobjects.base.activity.param.ActivityParam;
import valueobjects.base.activity.result.ActivityResult;

public interface IActivityQualificationProvider {

	/**
	 * 筛选器名称，注明清楚规则的主要作用
	 * 
	 * @return
	 */
	public String getName();

	public int getPriority();

	/**
	 * 筛选器参数，通过这可以知道这个规则是需要什么参数;
	 * 
	 * @return
	 */
	public Class<?> getParam();

	/**
	 *
	 * @param activityContext
	 * @return
	 */
	public ActivityResult match(ActivityContext activityContext);
}
