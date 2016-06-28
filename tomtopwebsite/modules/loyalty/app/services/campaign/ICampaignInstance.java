package services.campaign;

import java.util.List;

public interface ICampaignInstance {

	String getInstanceId();

	ICampaign getCampaign();

	List<IActionRuleParameter> getActionRuleParams();

	List<IActionParameter> getActionParams();

	ActionRules getActionRules();

	List<IAction> getActions();

	void persist();
}
