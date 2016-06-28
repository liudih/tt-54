package extensions.activity;

import java.util.List;
import java.util.Map;
import java.util.Set;

import play.Logger;
import services.base.message.MessageService;
import valueobjects.base.activity.ActivityComponentParam;
import valueobjects.base.activity.ActivityContext;
import valueobjects.base.activity.ActivityStatus;
import valueobjects.base.activity.LotteryResult;
import valueobjects.base.activity.param.JoinActivityParam;
import valueobjects.base.activity.result.ActivityResult;
import valueobjects.base.activity.rule.ActivityRuleResult;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.inject.Inject;

public class ActivityComponent {

	@Inject
	Set<IActivityQualificationProvider> activityQualificationSet;
	@Inject
	Set<IActivityRuleProvider> activityRuleSet;
	@Inject
	Set<IActivityPrizeProvider> activityPrizeSet;

	@Inject
	MessageService messageService;

	public Set<IActivityQualificationProvider> getQualificationSet() {
		return this.activityQualificationSet;
	}

	public Set<IActivityRuleProvider> getRuleSet() {
		return this.activityRuleSet;
	}

	public Set<IActivityPrizeProvider> getPrizeSet() {
		return this.activityPrizeSet;
	}

	/**
	 * 筛选
	 * 
	 * @param activityContext
	 * @param qualificationList
	 * @return
	 */
	public ActivityResult execQualification(ActivityContext activityContext,
			List<ActivityComponentParam> qualificationlist) {
		ActivityResult cstatus = null;
		JoinActivityParam jap = new JoinActivityParam();
		if (null == activityContext || null == qualificationlist || qualificationlist.size() == 0) {
			Logger.error("exec activity Qualification error : activityContext is null || qualificationList is null ");
			return new ActivityResult(ActivityStatus.FAILED, jap);
		}
		Map<String, IActivityQualificationProvider> qmap = Maps.uniqueIndex(activityQualificationSet,
				p -> p.getClass().getName());
		qualificationlist.sort((p1, p2) -> Integer.compare(p1.getPriority(), p2.getPriority()));
		for (ActivityComponentParam qual : qualificationlist) {
			if (qmap.containsKey(qual.getClassName())) {
				activityContext.changeComponentParam(qual);
				cstatus = qmap.get(qual.getClassName()).match(activityContext);
				if (null == cstatus.getJoinParam()) {
					cstatus.setJoinParam(new JoinActivityParam());
				} else {
					jap = cstatus.getJoinParam();
				}
				if (cstatus.getFixedStatus() != ActivityStatus.SUCC) {
					return cstatus;
				}
			} else {
				Logger.error("exec activity Qualification error : not found  Qualification: {} ", qual.getClassName());
				return new ActivityResult(ActivityStatus.FAILED, jap);
			}
		}
		activityContext.changeComponentParam(null);
		return new ActivityResult(ActivityStatus.SUCC, jap);
	}

	/**
	 * 规则
	 * 
	 * @param activityContext
	 * @param prizesParams
	 * @return
	 */
	public List<ActivityRuleResult> execRule(ActivityContext activityContext, List<ActivityComponentParam> rulelist,
			List<ActivityComponentParam> prizeList) {
		if (null == activityContext || null == rulelist || rulelist.size() == 0) {
			Logger.error("exec activity rule error : activityContext is null || ruleMap is null ");
			return Lists.newArrayList();
		}

		Map<String, IActivityRuleProvider> rmap = Maps.uniqueIndex(activityRuleSet, p -> p.getClass().getName());

		rulelist.sort((p1, p2) -> Integer.compare(p1.getPriority(), p2.getPriority()));

		List<ActivityRuleResult> ruleResult = Lists.newArrayList();
		for (ActivityComponentParam aparam : rulelist) {
			if (rmap.containsKey(aparam.getClassName())) {
				activityContext.changeComponentParam(aparam);
				Map<String, IActivityPrizeProvider> pmap = Maps.uniqueIndex(activityPrizeSet,
						p -> p.getClass().getName());

				prizeList.sort((p1, p2) -> Integer.compare(p1.getPriority(), p2.getPriority()));
				ActivityRuleResult tresult = rmap.get(aparam.getClassName()).execute(activityContext, prizeList, pmap);
				if (tresult.isPassed() == false) {
					Logger.warn("not pass rule {}", aparam.getClassName());
				}
				ruleResult.add(tresult);
			} else {
				Logger.error("exec activity rule error : not found  rule: {} ", aparam.getClassName());
				return Lists.newArrayList();
			}
		}

		activityContext.changeComponentParam(null);
		return ruleResult;
	}

	public ActivityResult execPrize(ActivityContext activityContext, List<ActivityComponentParam> prizeList,
			List<ActivityRuleResult> activityRuleResultList) {
		if (null == activityContext || null == prizeList || prizeList.size() == 0) {
			Logger.error("exec Prize  error : activityContext is null || prizeMap is null ");
			return new ActivityResult(ActivityStatus.FAILED);
		}

		if (null == com.google.common.collect.FluentIterable.from(activityRuleResultList)
				.firstMatch(p -> p.isPassed())) {
			Logger.error("exec Prize rule error : not rule pass ");
			return new ActivityResult(ActivityStatus.FAILED);
		}

		Map<String, IActivityPrizeProvider> pmap = Maps.uniqueIndex(activityPrizeSet, p -> p.getClass().getName());

		prizeList.sort((p1, p2) -> Integer.compare(p1.getPriority(), p2.getPriority()));
		ActivityComponentParam aparam = activityRuleResultList.get(0).getAcp();
		/*
		 * for (ActivityComponentParam aparam : prizeList) { if
		 * (pmap.containsKey(aparam.getClassName())) {
		 */
		activityContext.changeComponentParam(aparam);
		Logger.debug("param:" + activityContext.getActivityComponentParam().getParam());
		Logger.debug("extendsParam:" + activityContext.getActivityComponentParam().getExtendsParam());
		ActivityResult aresult = pmap.get(aparam.getClassName()).match(activityContext, activityRuleResultList.get(0));
		if (aresult.getFixedStatus() == ActivityStatus.SUCC) {
			LotteryResult lr = new LotteryResult();
			lr.setLotteryLevel(aparam.getPriority());
			lr.setDesc(aparam.getName());
			Logger.debug("prizeName:"+aparam.getName());
			aresult.setLotteryResult(lr);
			return aresult;
		}
		/*
		 * } else { Logger.error(
		 * "exec activity prize error : not found  prize: {} ",
		 * aparam.getClassName()); return new
		 * ActivityResult(ActivityStatus.FAILED); } }
		 */
		return new ActivityResult(ActivityStatus.FAILED);
	}
}