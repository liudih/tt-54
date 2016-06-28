package services.campaign;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import play.Logger;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Maps;

public class ActionRulesParameterCodec extends
		SimpleJsonCodec<IActionRuleParameter> {

	final List<IActionRule> rules;
	final Map<String, IActionRule> ruleMap;

	public ActionRulesParameterCodec(List<IActionRule> rules) {
		this.rules = rules;
		this.ruleMap = Maps.uniqueIndex(rules, r -> r.getId());
	}

	@Override
	public Class<ActionRulesParameter> getSourceClass() {
		return ActionRulesParameter.class;
	}

	@Override
	public JsonNode encode(IActionRuleParameter fromObj) {
		ActionRulesParameter p = (ActionRulesParameter) fromObj;
		Map<String, IActionRuleParameter> parameterMap = Maps.uniqueIndex(
				p.getParameters(), x -> x.getActionRuleId());
		// delegate to action
		Map<String, JsonNode> jsonMap = Maps.transformValues(ruleMap, r -> r
				.getParameterCodec().encode(parameterMap.get(r.getId())));
		return objectMapper.convertValue(jsonMap, JsonNode.class);
	}

	@Override
	public IActionRuleParameter decode(JsonNode fromObj) {
		TypeReference<HashMap<String, JsonNode>> typeRef = new TypeReference<HashMap<String, JsonNode>>() {
		};
		Map<String, JsonNode> jsonMap = objectMapper.convertValue(fromObj,
				typeRef);
		Logger.debug("Map parsed: {}", jsonMap);
		Map<String, IActionRuleParameter> params = Maps.transformValues(
				ruleMap,
				r -> r.getParameterCodec().decode(jsonMap.get(r.getId())));
		return new ActionRulesParameter(FluentIterable.from(params.values())
				.filter(p -> p != null).toList());
	}
}
