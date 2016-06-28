package forms.mobile;

import play.data.validation.Constraints.Required;

public class AlterPwdForm {

	@Required
	private String pwd;

	@Required
	private String email;

	@Required
	private String code;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

}
