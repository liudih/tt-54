package services.campaign;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;

import play.Logger;
import play.libs.F;
import services.campaign.MultiRules.Match;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public abstract class CampaignSupport implements ICampaign {

	@Inject
	protected ActionRuleRepository ruleRepo;

	@Inject
	protected ActionRepository actionRepo;

	protected ObjectMapper objectMapper = new ObjectMapper();

	protected abstract List<String> getPossibleActionRuleIDs();

	protected abstract List<String> getPossibleActionIDs();

	// --------- helper implementations ----------
	@Override
	public List<IActionRule> getPossibleActionRules() {
		return Lists.transform(getPossibleActionRuleIDs(),
				s -> ruleRepo.findRule(s));
	}

	@Override
	public List<IAction> getPossibleActions() {
		return Lists.transform(getPossibleActionIDs(),
				s -> actionRepo.findAction(s));
	}

	/**
	 * 获取注册的Action
	 * @author lijun
	 * @param actionIds
	 * @return
	 */
	public List<IAction> getActions(List<String> actionIds) {
		return Lists.transform(actionIds,
				s -> actionRepo.findAction(s));
	}
	
	protected F.Tuple4<ActionRules, ActionRulesParameter, List<IAction>, List<IActionParameter>> convert(
			String ruleIDCSV, String ruleParamsJSON, String actionIDCSV,
			Match match, String actionParamsJSON) throws Exception {

		ActionRules actionRules = null;
		ActionRulesParameter actionRulesParams = null;

		if (!StringUtils.isEmpty(ruleIDCSV)) {
			List<IActionRule> rules = FluentIterable
					.from(Arrays.asList(ruleIDCSV.split(",")))
					.transform(s -> ruleRepo.findRule(s)).toList();
			actionRules = new ActionRules(rules, match);

			if (!StringUtils.isEmpty(ruleParamsJSON)) {
				JsonNode node = objectMapper.readTree(ruleParamsJSON);
				actionRulesParams = (ActionRulesParameter) actionRules
						.getParameterCodec().decode(node);
				Logger.debug("ActionRuleParams Parsed: {}", actionRulesParams);
			}
		}

		List<IAction> actions = null;
		List<IActionParameter> actionParams = null;

		if (!StringUtils.isEmpty(actionIDCSV)) {
			actions = FluentIterable
					.from(Arrays.asList(actionIDCSV.split(",")))
					.transform(s -> actionRepo.findAction(s)).toList();

			Map<String, IAction> actionById = Maps.uniqueIndex(actions,
					a -> a.getId());

			if (!StringUtils.isEmpty(actionParamsJSON)) {
				JsonNode node = objectMapper.readTree(actionParamsJSON);
				actionParams = FluentIterable
						.from(Lists.newArrayList(node.fields()))
						.transform(
								e -> {
									IAction action = actionById.get(e.getKey());
									JsonNode n = e.getValue();
									if (action != null
											&& action.getParameterCodec() != null) {
										IActionParameter actionParam = action
												.getParameterCodec().decode(n);
										return actionParam;
									}
									return null;
								}).filter(p -> p != null).toList();
			}
		}
		return F.Tuple4(actionRules, actionRulesParams, actions, actionParams);
	}
}
