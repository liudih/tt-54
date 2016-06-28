package forms.mobile;

import play.data.validation.Constraints.Required;

public class ResetPwdForm {

	@Required
	private String oldpwd;

	@Required
	private String pwd;

	public String getOldpwd() {
		return oldpwd;
	}

	public void setOldpwd(String oldpwd) {
		this.oldpwd = oldpwd;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

}
