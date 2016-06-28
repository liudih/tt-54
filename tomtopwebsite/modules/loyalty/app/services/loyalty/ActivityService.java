package services.loyalty;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import mapper.loyalty.ActivityMapper;
import mapper.loyalty.IntegralBehaviorMapper;

import org.apache.commons.beanutils.PropertyUtils;

import play.Logger;
import services.base.utils.DoubleCalculateUtils;
import services.base.utils.StringUtils;
import services.loyalty.award.IAwardProvider;

import com.google.common.collect.Maps;

import entity.loyalty.Activity;
import entity.loyalty.DotypeRuleElement;

public class ActivityService {
	@Inject
	Set<IAwardProvider> providers;

	@Inject
	ActivityMapper activitymapper;

	@Inject
	IntegralBehaviorMapper inm;

	@Inject
	IPointsService service;

	@Inject
	ActivityService activityService;

	@Inject
	AwardService awardService;

	public IAwardProvider getAwardByAwardtype(String awardtype) {
		if (StringUtils.isEmpty(awardtype)) {
			return null;
		}
		Map<String, IAwardProvider> map = Maps.uniqueIndex(providers,
				e -> e.awardtype());
		return map.get(awardtype);
	}

	public Map<String, String> getRuleElement(String Cdotype) {
		Map<String, String> elementmap = new HashMap<String, String>();
		List<DotypeRuleElement> ruleelement = activitymapper
				.getRuleElement(Cdotype);
		for (DotypeRuleElement rule : ruleelement) {
			elementmap.put(rule.getCshowname(), rule.getCreplacename());
		}
		elementmap.put("ceil", "Math.ceil");
		elementmap.put("round", "Math.round");
		elementmap.put("floor", "Math.floor");

		return elementmap;
	}

	public void runRule(Integer websiteid, String activityName, Object object,
			String email) throws Exception {
		Activity activity = activitymapper.getActivityFor(activityName,
				websiteid);
		if (activity != null) {
			Map<String, Object> objectmap = PropertyUtils.describe(object);

			Map<String, String> mapx = activityService
					.getRuleElement(activityName);
			String rule = activity.getCrule();
			for (String key : mapx.keySet()) {
				rule = rule.replaceAll(key, mapx.get(key));
			}
			Logger.debug("-----------------rule-----------------:" + rule);
			for (String key : objectmap.keySet()) {
				if (objectmap.get(key) != null) {
					rule = rule.replaceAll(key, objectmap.get(key).toString());
				}
			}
			ScriptEngineManager manager = new ScriptEngineManager(
					ClassLoader.getSystemClassLoader());
			ScriptEngine engine = manager.getEngineByName("js");
			Double cost = new Double(String.valueOf(engine.eval(rule)));
			DoubleCalculateUtils duti = new DoubleCalculateUtils(cost);
			Double h = 1.0;
			Logger.debug("-----------------value-----------------:"
					+ duti.multiply(h).doubleValue());
			Double value = duti.multiply(h).doubleValue();
			IAwardProvider iAwardProvider = awardService
					.getIAwardProviderByType(activity.getCawardtype());
			iAwardProvider.runAward(websiteid, email, value,
					activity.getCawardtype());
		}
	}

}
