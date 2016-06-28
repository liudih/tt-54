package services.campaign;

import java.util.List;

public class ActionRulesParameter implements IActionRuleParameter {

	List<IActionRuleParameter> params;

	public ActionRulesParameter() {
	}

	public ActionRulesParameter(List<IActionRuleParameter> params) {
		this.params = params;
	}

	public List<IActionRuleParameter> getParameters() {
		return params;
	}

	public void setParameters(List<IActionRuleParameter> params) {
		this.params = params;
	}

	@Override
	public String getActionRuleId() {
		return ActionRules.ID;
	}

	public void setActionRuleId(String id) {
		// no-op
	}

	@Override
	public String toString() {
		return "ActionRulesParameter [params=" + params + "]";
	}

}
