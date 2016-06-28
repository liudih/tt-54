package services.research.activity.qualification.provider;

import play.Logger;

import com.google.inject.Inject;

import services.base.FoundationService;
import valueobjects.base.LoginContext;
import valueobjects.base.activity.ActivityContext;
import valueobjects.base.activity.ActivityStatus;
import valueobjects.base.activity.param.ActivityParam;
import valueobjects.base.activity.result.ActivityResult;
import extensions.activity.IActivityQualificationProvider;

public class LoginActivityQualificationProvider implements
		IActivityQualificationProvider {

	@Inject
	FoundationService foudactionService;

	@Override
	public String getName() {
		return "login";
	}

	@Override
	public int getPriority() {
		return 20;
	}

	@Override
	public ActivityResult match(ActivityContext activityContext) {
		LoginContext lcontext = foudactionService.getLoginContext();
		if (null != lcontext) {
			Logger.debug("is--->login {}", lcontext.isLogin());
			if (lcontext.isLogin()) {
				return new ActivityResult(ActivityStatus.SUCC);
			}else{
				return new ActivityResult(ActivityStatus.NO_LOGIN);
			}
		}
		return new ActivityResult(ActivityStatus.SUCC);
	}

	@Override
	public  Class<?> getParam() {
		// TODO Auto-generated method stub
		return ActivityParam.class;
	}
}
