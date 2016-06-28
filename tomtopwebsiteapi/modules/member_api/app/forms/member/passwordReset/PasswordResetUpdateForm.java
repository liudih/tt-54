package forms.member.passwordReset;

import java.io.Serializable;

import play.data.validation.Constraints.Required;

public class PasswordResetUpdateForm implements Serializable {
	private static final long serialVersionUID = 1L;
	String email;
	@Required
	String cid;
	@Required
	String passwd;
	@Required
	String confirm_password;

	@Override
	public String toString() {
		return "PasswordResetUpdateForm [email=" + email + ", cid=" + cid
				+ ", passwd=" + passwd + ", confirm_password="
				+ confirm_password + "]";
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPasswd() {
		return passwd;
	}

	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}

	public String getConfirm_password() {
		return confirm_password;
	}

	public void setConfirm_password(String confirm_password) {
		this.confirm_password = confirm_password;
	}

	public String getCid() {
		return cid;
	}

	public void setCid(String cid) {
		this.cid = cid;
	}

}
