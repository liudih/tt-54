package controllers.research;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.inject.Inject;

import entity.activity.page.PagePrize;
import entity.activity.page.PageQualification;
import entity.activity.page.PageRule;
import play.Logger;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;
import service.activity.IPageService;
import services.base.FoundationService;
import services.base.utils.StringUtils;
import services.research.activity.ActivityService;
import services.research.activity.prize.provider.VoteActivityPrize;
import services.research.activity.qualification.provider.UnLimitActivityQualificationProvider;
import services.research.activity.rule.provider.UnActivityRuleProvider;
import valueobjects.base.activity.ActivityComponentParam;
import valueobjects.base.activity.ActivityContext;
import valueobjects.base.activity.ActivityStatus;
import valueobjects.base.activity.param.ActivityParam;
import valueobjects.base.activity.result.ActivityResult;

public class ActivityController extends Controller {

	@Inject
	ActivityService activityService;
	@Inject
	FoundationService foundationservice;
	@Inject
	IPageService pageService;

	/**
	 * {"activityType":"1","activityPageId":1,"activityPageItemId":2,"endDate":
	 * "yyyy-MM-dd HH:mm:ss"}
	 * 
	 * @return
	 */
	@BodyParser.Of(BodyParser.Json.class)
	public Result recordAction() {
		ActivityResult afixResult = new ActivityResult(ActivityStatus.FAILED);
		try {
			JsonNode jnode = request().body().asJson();
			if (jnode != null) {
				String activityType = jnode.get("activityType").asText();
				int activityPageId = jnode.get("activityPageId").asInt();
				int activityPageItemId = jnode.get("activityPageItemId")
						.asInt();
				Date bdate = new Date();
				Date edate = new Date();
				try {
					SimpleDateFormat df = new SimpleDateFormat(
							"yyyy-MM-dd HH:mm:ss");
					String startdateStr = jnode.get("startDate").asText();
					bdate = df.parse(startdateStr);
					String enddateStr = jnode.get("endDate").asText();
					edate = df.parse(enddateStr);
				} catch (Exception ex) {
					Logger.warn("hot point can not get end date");
					return ok(Json.toJson(afixResult));
				}
				List<ActivityComponentParam> qparams = getQualificationMap(activityPageId);
				List<ActivityComponentParam> rparams = getRuleMap(activityPageId);
				List<ActivityComponentParam> pparams = getPrizeMap(activityPageId);
				ActivityContext activityContext = new ActivityContext(
						activityType, activityPageId, activityPageItemId,
						foundationservice.getSiteID(), edate, bdate, null);
				afixResult = activityService.join(activityContext, qparams,
						rparams, pparams);
			}
		} catch (Exception ex) {
			Logger.error("hit point", ex);
		}
		return ok(Json.toJson(afixResult));
	}

	/**
	 * 
	 * @Title: getQualificationMap
	 * @Description: TODO(获取筛选条件)
	 * @param @param pageId
	 * @param @return
	 * @return Map<String,String>
	 * @throws
	 * @author yinfei
	 */
	private List<ActivityComponentParam> getQualificationMap(int pageId) {
		List<ActivityComponentParam> params = Lists.newArrayList();
		List<PageQualification> pageQualificationList = pageService
				.getPQByPageId(pageId);
		if (null != pageQualificationList && pageQualificationList.size() > 0) {
			for (PageQualification pq : pageQualificationList) {
				ActivityComponentParam parm = new ActivityComponentParam(
						pq.getIid(), pq.getCrule(), pq.getCruleparam(), null,
						pq.getIsort(),null);
				params.add(parm);
			}
		}
		return params;
	}

	/**
	 * 
	 * @Title: getRuleMap
	 * @Description: TODO(获取活动规则)
	 * @param @param pageId
	 * @param @return
	 * @return Map<String,String>
	 * @throws
	 * @author yinfei
	 */
	private List<ActivityComponentParam> getRuleMap(int pageId) {
		List<ActivityComponentParam> params = Lists.newArrayList();
		List<PageRule> pageRuleList = pageService.getPageRuleByPageId(pageId);
		if (null != pageRuleList && pageRuleList.size() > 0) {
			for (PageRule pr : pageRuleList) {
				ActivityComponentParam parm = new ActivityComponentParam(
						pr.getIid(), pr.getCrule(), pr.getCruleparam(), null,
						pr.getIid(),null);
				params.add(parm);
			}
		}
		return params;
	}

	/**
	 * 
	 * @Title: getPrizeMap
	 * @Description: TODO(获取活动结果)
	 * @param @param pageId
	 * @param @return
	 * @return Map<String,Map<String,String>>
	 * @throws
	 * @author yinfei
	 */
	private List<ActivityComponentParam> getPrizeMap(int pageId) {
		List<ActivityComponentParam> params = Lists.newArrayList();
		List<PagePrize> pagePrizeList = pageService.getPPByPageId(pageId);
		if (null != pagePrizeList && pagePrizeList.size() > 0) {
			for (PagePrize pp : pagePrizeList) {
				ActivityComponentParam parm = new ActivityComponentParam(
						pp.getIid(), pp.getCtype(), pp.getCtypeparam(),
						pp.getCextraparam(), pp.getIsort(),pp.getCname());
				params.add(parm);
			}
		}
		return params;
	}

	public Result getActivityPrize() {
		Map<String, ActivityParam> qualificationMap = Maps.newHashMap();
		Map<String, ActivityParam> ruleMap = Maps.newHashMap();
		return ok();
	}

}
