package services.research.activity.prize.provider;

import java.io.IOException;
import java.util.Date;

import play.Logger;
import play.Logger.ALogger;
import services.base.FoundationService;
import valueobjects.base.activity.ActivityContext;
import valueobjects.base.activity.ActivityStatus;
import valueobjects.base.activity.param.ActivityParam;
import valueobjects.base.activity.param.PercentActivityParam;
import valueobjects.base.activity.param.PrizeActivityParam;
import valueobjects.base.activity.result.ActivityResult;
import valueobjects.base.activity.rule.ActivityRuleResult;
import valueobjects.base.activity.rule.ProbabilityActivityRuleResult;
import values.activity.page.PagePrizeResultQuery;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;

import dao.activitydb.page.impl.PagePrizeResultDaoImpl;
import entity.activity.page.PagePrizeResult;
import extensions.activity.IActivityPrizeProvider;

/**
 * 奖品类型基类
 * 
 * @author liulj
 *
 */
public abstract class ActivityPrize implements IActivityPrizeProvider {

	ALogger logger = Logger.of(this.getClass());

	@Inject
	PagePrizeResultDaoImpl prizeResultDao;

	@Inject
	FoundationService foundationService;

	/**
	 * 验证参数是否通过，验证抽奖规则是否通过，抽奖时间是否通过
	 * 
	 * @param activityContext
	 * @param activityRuleResult
	 * @param param
	 *            传出已经转换的参数值
	 * @return 成功返回传参数 否则 返回null
	 */
	public PrizeActivityParam validate(ActivityContext activityContext, ActivityRuleResult activityRuleResult) {
		if (activityRuleResult.isPassed() == false) {
			logger.warn("rule not pass--》{}", this.getClass().getName());
			return null;
		}
		if (activityContext.getActivityComponentParam().getParam() == null) {
			logger.error("match order refund error:  not found {} param  ", this.getClass().getName());
			return null;
		}
		PrizeActivityParam param = null;
		try {
			param = (PrizeActivityParam) new ObjectMapper()
					.readValue(activityContext.getActivityComponentParam().getParam(), getParam());
		} catch (IOException e) {
			logger.error("param json fail:" + e.getMessage());
			return null;
		}
		if (param == null) {
			logger.error("is null ", this.getClass().getName());
			return null;
		}
		Date currenteDate = new Date();
		if (param.getBeginDate() != null && !currenteDate.after(param.getBeginDate())) {
			Logger.warn("match  begintime not arrive  ", this.getClass().getName());
			return null;
		}
		if (param.getEndDate() != null && !currenteDate.before(param.getEndDate())) {
			Logger.warn("match endtime obsolete ", this.getClass().getName());
			return null;
		}
		return param;
	}

	@Override
	public ActivityResult match(ActivityContext activityContext, ActivityRuleResult activityRuleResult) {
		ObjectMapper om = new ObjectMapper();
		PrizeActivityParam param = validate(activityContext, activityRuleResult);
		if (param == null) {
			return new ActivityResult(ActivityStatus.FAILED);
		}
		PagePrizeResultQuery query = new PagePrizeResultQuery();
		query.setIpageid(activityContext.getActivityPageId());
		query.setIprizeid(activityContext.getActivityComponentParam().getId());
		PagePrizeResult result = new PagePrizeResult();
		result.setIpageid(activityContext.getActivityPageId());
		result.setIprizeid(query.getIprizeid());
		result.setCemail(foundationService.getLoginContext().getMemberID());
		result.setCvhost(foundationService.getVhost());
		result.setDcreatedate(new Date());
		result.setCcountry(foundationService.getCountry());
		result.setIwebsiteid(activityContext.getWebsiteId());
		result.setCprizevalue(getCprizeValue(activityContext, activityRuleResult, param));
		int row = 0;
		/*
		 * PercentActivityParam ap = null; try { ap =
		 * om.readValue(activityContext.getActivityComponentParam().
		 * getExtendsParam(), PercentActivityParam.class); } catch (Exception e)
		 * { Logger.error("json parse error", e); }
		 */
		synchronized (this) {
			// if (activityRuleResult.checking(ap)) {
			int count = prizeResultDao.getPagePrizeLotteryCount(query);
			Logger.debug("givedPrizeCount:" + count);
			if (param.getNumber() > count) {
				row = prizeResultDao.add(result);
				if (row > 0) {
					return onAddRecordSuccess(activityContext, activityRuleResult, param, result);
				}
			} else {
				Logger.warn("prizes amount is over!");
				return new ActivityResult(ActivityStatus.NO_PRIZES);
			}
			/*
			 * } else { return new ActivityResult(ActivityStatus.NO_PRIZES); }
			 */
		}
		return new ActivityResult(ActivityStatus.FAILED);
	}

	/**
	 * 获取发放的奖品值
	 * 
	 * @param activityContext
	 * @param activityRuleResult
	 * @param param
	 * @return
	 */
	public abstract String getCprizeValue(ActivityContext activityContext, ActivityRuleResult activityRuleResult,
			PrizeActivityParam param);

	/**
	 * 在新增发放奖品记录成功后调用的方法
	 * 
	 * @param activityContext
	 * @param activityRuleResult
	 * @param param
	 * @return
	 */
	public abstract ActivityResult onAddRecordSuccess(ActivityContext activityContext,
			ActivityRuleResult activityRuleResult, PrizeActivityParam param, PagePrizeResult result);
}
