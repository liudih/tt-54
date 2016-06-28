package services.research.activity.rule.provider;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import play.Logger;
import valueobjects.base.activity.ActivityComponentParam;
import valueobjects.base.activity.ActivityContext;
import valueobjects.base.activity.ActivityStatus;
import valueobjects.base.activity.param.ActivityParam;
import valueobjects.base.activity.param.PercentActivityParam;
import valueobjects.base.activity.param.ProbabilityActivityParam;
import valueobjects.base.activity.result.ActivityResult;
import valueobjects.base.activity.rule.ActivityRuleResult;
import valueobjects.base.activity.rule.ProbabilityActivityRuleResult;
import extensions.activity.IActivityPrizeProvider;
import extensions.activity.IActivityRuleProvider;

/**
 * 概率规则，根据规则返回概率的值
 * 
 * @author fcl
 *
 */
public class ProbabilityActivityRuleProvider implements IActivityRuleProvider {

	@Override
	public String getName() {
		return "Probability-rule";
	}

	@Override
	public int getPriority() {
		return 10;
	}

	@Override
	public Class<?> getParam() {
		return ProbabilityActivityParam.class;
	}

	@Override
	public ActivityRuleResult execute(ActivityContext activityContext, List<ActivityComponentParam> prizeList, Map<String, IActivityPrizeProvider> pmap) {
		if (activityContext.getActivityComponentParam() != null
				&& activityContext.getActivityComponentParam().getParam() != null) {
			ObjectMapper om = new ObjectMapper();
			ProbabilityActivityParam param;
			try {
				param = om.readValue(activityContext
						.getActivityComponentParam().getParam(),
						ProbabilityActivityParam.class);
				int max = param.getProbabilitybase();
				Random r = new Random();
				double percents = r.nextInt(max) / (double) max;
				Logger.debug(String.valueOf(percents));
				PercentActivityParam ap = null;
				ProbabilityActivityRuleResult par = new ProbabilityActivityRuleResult(true, percents);
				for (ActivityComponentParam aparam : prizeList) {
					if (pmap.containsKey(aparam.getClassName())) {
						activityContext.changeComponentParam(aparam);
						ap = om.readValue(activityContext.getActivityComponentParam().getExtendsParam(),
								PercentActivityParam.class);
						Logger.debug("prize percent:"+ap.getPercent());
						if(par.checking(ap)){
							par.setAcp(aparam);
							break;
						};
					}
				}
				return par;
			} catch (Exception ex) {
				Logger.error("probability rule error : ", ex);
			}
		}
		return new ProbabilityActivityRuleResult(false, 0.0);
	}

	@Override
	public Class<?> getPrizeParam() {
		return new ProbabilityActivityRuleResult(true, 0.0).getPrizeParam();
	}

}
