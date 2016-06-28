package extensions.loyalty.campaign.action.discount;

import java.util.List;

import services.campaign.IActionParameter;

public class SimpleDiscountActionParameter implements IActionParameter {

	DiscountType type;

	DiscountUnit unit;

	double value;

	List<String> includeSku;

	List<String> excludeSku;

	@Override
	public String getActionId() {
		return SimpleDiscountAction.ID;
	}

	// not used
	public void setActionId(String id) {
	}

	public DiscountType getType() {
		return type;
	}

	public void setType(DiscountType type) {
		this.type = type;
	}

	public DiscountUnit getUnit() {
		return unit;
	}

	public void setUnit(DiscountUnit unit) {
		this.unit = unit;
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}

	public List<String> getIncludeSku() {
		return includeSku;
	}

	public void setIncludeSku(List<String> includeSku) {
		this.includeSku = includeSku;
	}

	public List<String> getExcludeSku() {
		return excludeSku;
	}

	public void setExcludeSku(List<String> excludeSku) {
		this.excludeSku = excludeSku;
	}

}
