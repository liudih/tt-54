package services.campaign;

import codec.ICodec;

import com.fasterxml.jackson.databind.JsonNode;

public interface IActionRule {

	String getId();

	boolean match(CampaignContext context, IActionRuleParameter param);

	ICodec<IActionRuleParameter, JsonNode> getParameterCodec();
}
