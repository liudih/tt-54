package forms.member.register;

import java.io.Serializable;

import play.data.validation.Constraints.Required;

public class RegisterForm implements Serializable {
	private static final long serialVersionUID = 1L;

	@Required
	String email;

	@Required
	String passwd;

	@Required
	String country;

	String code;

	boolean signupNewsletter;
	
	String firstname;
	
	String lastname;

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

	public String getPasswd() {
		return passwd;
	}

	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public boolean isSignupNewsletter() {
		return signupNewsletter;
	}

	public void setSignupNewsletter(boolean signupNewsletter) {
		this.signupNewsletter = signupNewsletter;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}
	
}
