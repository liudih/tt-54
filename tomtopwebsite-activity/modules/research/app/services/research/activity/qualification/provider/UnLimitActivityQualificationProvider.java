package services.research.activity.qualification.provider;

import valueobjects.base.activity.ActivityContext;
import valueobjects.base.activity.ActivityStatus;
import valueobjects.base.activity.param.ActivityParam;
import valueobjects.base.activity.result.ActivityResult;
import extensions.activity.IActivityQualificationProvider;

public class UnLimitActivityQualificationProvider implements
		IActivityQualificationProvider {

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "un-limit";
	}

	@Override
	public int getPriority() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Class<?> getParam() {
		// TODO Auto-generated method stub
		return ActivityParam.class;
	}

	@Override
	public ActivityResult match(ActivityContext activityContext) {
		// TODO Auto-generated method stub
		return new ActivityResult(ActivityStatus.SUCC);
	}

}
