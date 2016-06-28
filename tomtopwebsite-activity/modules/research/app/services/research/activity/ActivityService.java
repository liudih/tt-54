package services.research.activity;

import java.util.List;
import java.util.Map;

import org.elasticsearch.common.collect.Iterators;

import play.Logger;
import service.activity.IPageService;
import services.base.FoundationService;
import valueobjects.base.activity.ActivityComponentParam;
import valueobjects.base.activity.ActivityContext;
import valueobjects.base.activity.ActivityStatus;
import valueobjects.base.activity.param.ActivityParam;
import valueobjects.base.activity.result.ActivityResult;
import valueobjects.base.activity.result.VoteActivityResult;
import valueobjects.base.activity.rule.ActivityRuleResult;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;

import entity.activity.page.PageJoin;
import extensions.activity.ActivityComponent;

public class ActivityService {

	@Inject
	ActivityComponent activityComponent;

	@Inject
	IPageService pageService;

	@Inject
	FoundationService foudactionService;

	private ActivityResult canJoin(ActivityContext activityContext,
			List<ActivityComponentParam> qualificationParams) {
		return activityComponent.execQualification(activityContext,
				qualificationParams);
	}

	public ActivityResult join(ActivityContext activityContext,
			List<ActivityComponentParam> qualificationParams,
			List<ActivityComponentParam> ruleParams,
			List<ActivityComponentParam> prizesParams) {
		Logger.debug("activity --> Qualification ");
		ActivityResult qstatus = canJoin(activityContext, qualificationParams);
		if (qstatus.getFixedStatus() != ActivityStatus.SUCC) {
			return qstatus;
		}
		String joinParam = null;
		try {
			ObjectMapper om = new ObjectMapper();
			joinParam = om.writeValueAsString(qstatus.getJoinParam());
			// 新增活动参与的记录
			PageJoin pageJoin = new PageJoin();
			pageJoin.setIpageid(activityContext.getActivityPageId());
			pageJoin.setCcountry(foudactionService.getCountry());
			pageJoin.setCjoiner(foudactionService.getLoginContext().getMemberID());
			pageJoin.setCjoinparam(joinParam);
			pageJoin.setCresult(null);
			pageJoin.setIwebsiteid(foudactionService.getSiteID());
			pageJoin.setCvhost(null);
			pageService.addPageJoin(pageJoin);
		} catch (JsonProcessingException e) {
			Logger.error("JsonProcessingException-",e);
		}
		Logger.debug("activity --> rule ");
		List<ActivityRuleResult> ruleResultlist = activityComponent.execRule(
				activityContext, ruleParams, prizesParams);
		if(null != ruleResultlist.get(0).getAcp()){
			Logger.debug("activity --> save ");
			return activityComponent.execPrize(activityContext, prizesParams,
					ruleResultlist);
		}else{
			return new ActivityResult(ActivityStatus.NO_PRIZES);
		}
	}

}
