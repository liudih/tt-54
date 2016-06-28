package services.research.activity.qualification.provider;

import valueobjects.base.activity.ActivityContext;
import valueobjects.base.activity.ActivityStatus;
import valueobjects.base.activity.param.ActivityParam;
import valueobjects.base.activity.param.PointActivityParam;
import valueobjects.base.activity.result.ActivityResult;
import extensions.activity.IActivityQualificationProvider;

/**
 * 使用积分参与
 * @author fcl
 *
 */
public class UsingPointActivityQualificationProvider implements
		IActivityQualificationProvider {

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "using-point";
	}

	@Override
	public int getPriority() {
		// TODO Auto-generated method stub
		return 40;
	}

	@Override
	public Class<?> getParam() {
		// TODO Auto-generated method stub
		return PointActivityParam.class;
	}

	@Override
	public ActivityResult match(ActivityContext activityContext) {
		// TODO Auto-generated method stub
		
		return new ActivityResult(ActivityStatus.SUCC);
	}

}
