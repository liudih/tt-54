package services.research.activity.qualification.provider;

import java.util.Date;

import valueobjects.base.activity.ActivityContext;
import valueobjects.base.activity.ActivityStatus;
import valueobjects.base.activity.param.ActivityParam;
import valueobjects.base.activity.result.ActivityResult;
import extensions.activity.IActivityQualificationProvider;
import play.Logger;

public class ExpireActivityQualificationProvider implements
		IActivityQualificationProvider {

	@Override
	public String getName() {
		return "expire-enddate";
	}

	@Override
	public int getPriority() {
		return 10;
	}

	/*
	 * (non-Javadoc) <p>Title: match</p> <p>Description: 活动是否过期的规则校验</p>
	 * 
	 * @param activityContext
	 * 
	 * @return
	 * 
	 * @see
	 * extensions.activity.IActivityQualificationProvider#match(valueobjects
	 * .base.activity.ActivityContext)
	 */
	@Override
	public ActivityResult match(ActivityContext activityContext) {
		if (new Date().after(activityContext.getEndDate())
				|| new Date().after(activityContext.getBeginDate()) == false) {
			return new ActivityResult(ActivityStatus.EXOURE);
		}
		return new ActivityResult(ActivityStatus.SUCC);
	}

	@Override
	public Class<?> getParam() {
		return ActivityParam.class;
	}

}
