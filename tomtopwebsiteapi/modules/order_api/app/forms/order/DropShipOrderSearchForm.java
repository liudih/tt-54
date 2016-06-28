package forms.order;

import services.base.utils.StringUtils;

public class DropShipOrderSearchForm extends MemberOrderForm {
	String useremail; // dropship用户邮箱

	public String getUseremail() {
		if (StringUtils.isEmpty(useremail)) {
			return null;
		}
		return useremail.toLowerCase();
	}

	public void setUseremail(String useremail) {
		this.useremail = useremail;
	}

}
