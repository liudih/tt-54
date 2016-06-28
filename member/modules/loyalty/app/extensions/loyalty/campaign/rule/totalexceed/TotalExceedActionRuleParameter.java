package extensions.loyalty.campaign.rule.totalexceed;

import services.campaign.IActionRuleParameter;

public class TotalExceedActionRuleParameter implements IActionRuleParameter {

	double amount;
	String currency;

	@Override
	public String getActionRuleId() {
		return TotalExceedActionRule.ID;
	}

	public void setActionRuleId(String id) {
		// not used
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

}
