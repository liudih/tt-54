package services.campaign;

import java.util.List;
import java.util.Map;

import codec.ICodec;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Maps;

public class ActionRules extends MultiRules<IActionRule> implements IActionRule {

	public static final String ID = "multi-rules";

	final ActionRulesParameterCodec codec;

	public ActionRules(List<IActionRule> rules,
			services.campaign.MultiRules.Match match) {
		super(rules, match);
		this.codec = new ActionRulesParameterCodec(rules);
	}

	@Override
	public boolean match(CampaignContext context, IActionRuleParameter param) {
		ActionRulesParameter multi = (ActionRulesParameter) param;
		Map<String, IActionRuleParameter> pmap = Maps.uniqueIndex(
				multi.getParameters(),
				(IActionRuleParameter p) -> p.getActionRuleId());
		switch (getMatch()) {
		case MATCH_ALL:
			return FluentIterable.from(getRules()).allMatch(
					r -> r.match(context, pmap.get(r.getId())));
		case MATCH_ONE:
			return FluentIterable.from(getRules()).anyMatch(
					r -> r.match(context, pmap.get(r.getId())));
		}
		return false;
	}

	public boolean match(CampaignContext context,
			List<IActionRuleParameter> params) {
		return match(context, new ActionRulesParameter(params));
	}

	@Override
	public String getId() {
		return ID;
	}

	@Override
	public ICodec<IActionRuleParameter, JsonNode> getParameterCodec() {
		return codec;
	}
}
