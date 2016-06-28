package forms.member.findPasswd;

import java.io.Serializable;

import play.data.validation.Constraints.Required;

public class FindPasswordForm implements Serializable {
	private static final long serialVersionUID = 1L;
	@Required
	private String email;

	@Override
	public String toString() {
		return "FindPasswordForm [email=" + email + "]";
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

}
