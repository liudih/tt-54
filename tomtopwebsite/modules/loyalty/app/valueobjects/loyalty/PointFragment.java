package valueobjects.loyalty;

public class PointFragment {
	private Integer memberPoints;
	private Integer costPoints;
	private String cartId;
	private Integer rulePoints;
	private Double ruleMoney;

	public PointFragment(Integer memberPoints, Integer costPoints, String cartId) {
		this.memberPoints = memberPoints;
		this.costPoints = costPoints;
		this.cartId = cartId;
	}

	public Integer getMemberPoints() {
		return memberPoints;
	}

	public void setMemberPoints(Integer memberPoints) {
		this.memberPoints = memberPoints;
	}

	public Integer getCostPoints() {
		return costPoints;
	}

	public void setCostPoints(Integer costPoints) {
		this.costPoints = costPoints;
	}

	public String getCartId() {
		return cartId;
	}

	public void setCartId(String cartId) {
		this.cartId = cartId;
	}

	public Integer getRulePoints() {
		return rulePoints;
	}

	public void setRulePoints(Integer rulePoints) {
		this.rulePoints = rulePoints;
	}

	public Double getRuleMoney() {
		return ruleMoney;
	}

	public void setRuleMoney(Double ruleMoney) {
		this.ruleMoney = ruleMoney;
	}
}
