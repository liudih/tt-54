package services.research.activity.qualification.provider;

import org.redisson.core.RSet;

import play.Logger;

import com.google.inject.Inject;

import services.base.FoundationService;
import valueobjects.base.LoginContext;
import valueobjects.base.activity.ActivityContext;
import valueobjects.base.activity.ActivityStatus;
import valueobjects.base.activity.param.ActivityParam;
import valueobjects.base.activity.result.ActivityResult;
import extensions.activity.IActivityQualificationProvider;
import extensions.base.activity.IActivitySessionService;

public class SingleJoinActivityqualificationProvider implements
		IActivityQualificationProvider {

	@Inject
	IActivitySessionService iActivitySessionService;
	@Inject
	FoundationService foudactionService;

	@Override
	public String getName() {
		return "single-join";
	}

	@Override
	public int getPriority() {
		return 30;
	}

	@Override
	public ActivityResult match(ActivityContext activityContext) {
		LoginContext loginContext = foudactionService.getLoginContext();
		if (loginContext != null && loginContext.isLogin()) {
			RSet<String> emails = iActivitySessionService.getSet(this
					.getkey(activityContext));
			emails.expireAt(activityContext.getEndDate());
			if (emails.contains(loginContext.getMemberID())) {
				Logger.debug("--->joined activity {}",
						loginContext.getMemberID());
				return new ActivityResult(ActivityStatus.JOINED);
			} else {
				emails.add(loginContext.getMemberID());
			}
			Logger.debug("--->can join activity");
			return new ActivityResult(ActivityStatus.SUCC);
		}
		return new ActivityResult(ActivityStatus.NO_LOGIN);
	}

	private String getkey(ActivityContext activityContext) {
		String key = getName();
		key += activityContext.getActivityPageId();
		key += activityContext.getActivityType();
		return key;
	}

	@Override
	public Class<?> getParam() {
		// TODO Auto-generated method stub
		return ActivityParam.class;
	}
}
