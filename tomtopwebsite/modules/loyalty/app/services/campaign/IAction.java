package services.campaign;

import codec.ICodec;

import com.fasterxml.jackson.databind.JsonNode;

public interface IAction {

	String getId();

	void execute(CampaignContext context, IActionParameter param);

	ICodec<IActionParameter, JsonNode> getParameterCodec();
}
