package forms.member.login;

import java.io.Serializable;

import javax.persistence.Id;

import org.mindrot.jbcrypt.BCrypt;

import play.data.validation.Constraints.Required;

public class LoginForm implements Serializable {
	private static final long serialVersionUID = 1L;

	@Required
	String email;

	@Required
	String password;

	public String getEmail() {
		return email;
	}

	public LoginForm() {
		super();
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String toString() {
		return "LoginForm [username=" + email + ", password=" + password
				+ "]";
	}

	 
}
