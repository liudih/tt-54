package forms.coupon;

import play.data.validation.Constraints.Required;

/**
 * 
 * @author lijun
 *
 */
public class CouponSendFrom {
	private int id;
//	@Required
	private int type;
	@Required
	private int ruleId;
	private String email;

	public int getType() {
		return type;
	}

	public int getId() {
		return id;
	}

	public void setId(Integer id) {
		if(id == null){
			this.id = 0;
		}else{
			this.id = id;
		}
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getRuleId() {
		return ruleId;
	}

	public void setRuleId(int ruleId) {
		this.ruleId = ruleId;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

}
