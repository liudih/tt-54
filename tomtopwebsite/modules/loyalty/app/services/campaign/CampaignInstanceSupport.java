package services.campaign;

import java.util.List;

import services.campaign.MultiRules.Match;

import com.google.common.collect.Lists;

public abstract class CampaignInstanceSupport implements ICampaignInstance {

	protected String instanceId;
	protected ICampaign campaign;
	protected List<IActionRuleParameter> actionRuleParams = Lists
			.newLinkedList();
	protected List<IActionParameter> actionParams = Lists.newLinkedList();
	protected ActionRules actionRules = new ActionRules(Lists.newLinkedList(),
			Match.MATCH_ALL);
	protected List<IAction> actions = Lists.newLinkedList();

	@Override
	public String getInstanceId() {
		return instanceId;
	}

	@Override
	public ICampaign getCampaign() {
		return campaign;
	}

	@Override
	public List<IActionRuleParameter> getActionRuleParams() {
		return actionRuleParams;
	}

	@Override
	public List<IActionParameter> getActionParams() {
		return actionParams;
	}

	@Override
	public ActionRules getActionRules() {
		return actionRules;
	}

	@Override
	public List<IAction> getActions() {
		return actions;
	}

	public void setInstanceId(String instanceId) {
		this.instanceId = instanceId;
	}

	public void setCampaign(ICampaign campaign) {
		this.campaign = campaign;
	}

	public void setActionRuleParams(List<IActionRuleParameter> actionRuleParams) {
		this.actionRuleParams = actionRuleParams;
	}

	public void setActionParams(List<IActionParameter> actionParams) {
		this.actionParams = actionParams;
	}

	public void setActionRules(ActionRules actionRules) {
		this.actionRules = actionRules;
	}

	public void setActions(List<IAction> actions) {
		this.actions = actions;
	}

}
