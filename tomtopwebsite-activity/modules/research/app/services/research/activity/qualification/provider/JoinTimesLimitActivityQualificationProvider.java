package services.research.activity.qualification.provider;

import valueobjects.base.activity.ActivityContext;
import valueobjects.base.activity.ActivityStatus;
import valueobjects.base.activity.param.TimesActivityParam;
import valueobjects.base.activity.result.ActivityResult;

import java.io.IOException;

import javax.inject.Inject;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import extensions.activity.IActivityQualificationProvider;
import play.Logger;
import service.activity.IPageService;
import services.base.FoundationService;

/**
 * 参与次数限制
 * 
 * @author fcl
 *
 */
public class JoinTimesLimitActivityQualificationProvider implements IActivityQualificationProvider {

	@Inject
	IPageService pageService;
	@Inject
	FoundationService foudactionService;

	@Override
	public String getName() {
		return "join-times";
	}

	@Override
	public int getPriority() {
		return 80;
	}

	@Override
	public Class<?> getParam() {
		return TimesActivityParam.class;
	}

	@Override
	public ActivityResult match(ActivityContext activityContext) {
		TimesActivityParam param = null;
		ObjectMapper om = new ObjectMapper();
		if (activityContext.getActivityComponentParam() == null
				|| activityContext.getActivityComponentParam().getParam() == null) {
			Logger.error(" {} param can't be null", MaxJoinMemberQualification.class.getName());
			return new ActivityResult(ActivityStatus.FAILED);
		}
		try {
			param = om.readValue(activityContext.getActivityComponentParam().getParam(), TimesActivityParam.class);
		} catch (Exception e) {
			Logger.error("json parse error");
		}
		int joinTimes = pageService.getJoinedCount(activityContext.getActivityPageId(),
				foudactionService.getLoginContext().getMemberID(), foudactionService.getSiteID()).size();
		Logger.debug("times:" + param.getTimes());
		Logger.debug("joinTimes:" + joinTimes);
		if (joinTimes >= param.getTimes()) {
			return new ActivityResult(ActivityStatus.EXCEEDNUMBER);
		} else {
			return new ActivityResult(ActivityStatus.SUCC);
		}
	}

}
