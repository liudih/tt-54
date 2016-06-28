package services.research.activity.qualification.provider;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;

import extensions.activity.IActivityQualificationProvider;
import play.Logger;
import service.activity.IPageService;
import services.base.FoundationService;
import valueobjects.base.activity.ActivityContext;
import valueobjects.base.activity.ActivityStatus;
import valueobjects.base.activity.param.JoinMemberParam;
import valueobjects.base.activity.result.ActivityResult;

public class MaxJoinMemberQualification implements IActivityQualificationProvider {

	@Inject
	IPageService pageService;

	@Inject
	FoundationService foudactionService;

	@Override
	public String getName() {
		return "max Join Member";
	}

	@Override
	public int getPriority() {
		return 31;
	}

	@Override
	public Class<?> getParam() {
		return JoinMemberParam.class;
	}

	@Override
	public ActivityResult match(ActivityContext activityContext) {
		JoinMemberParam jmp = null;
		ObjectMapper om = new ObjectMapper();
		if (activityContext.getActivityComponentParam() == null
				|| activityContext.getActivityComponentParam().getParam() == null) {
			Logger.error(" {} param can't be null", MaxJoinMemberQualification.class.getName());
			return new ActivityResult(ActivityStatus.FAILED);
		}
		try {
			jmp = om.readValue(activityContext.getActivityComponentParam().getParam(), JoinMemberParam.class);
		} catch (Exception e) {
			Logger.error("json parse error");
		}
		int memberCount = pageService.getJoinMemberCount(activityContext.getActivityPageId(),
				foudactionService.getSiteID());
		Logger.debug("memberCount:" + memberCount);
		Logger.debug("maxmember:" + jmp.getMaxMember());
		if (memberCount > jmp.getMaxMember()) {
			return new ActivityResult(ActivityStatus.EXCEED_MEMBER_NUMBER);
		} else {
			return new ActivityResult(ActivityStatus.SUCC);
		}
	}

}
