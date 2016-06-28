package services.member.login;

import java.io.Serializable;

import dto.member.LoginEnum;

public class OtherLoginResult implements Serializable {

	private static final long serialVersionUID = 8365832442795306777L;

	final LoginEnum lenum;
	final String email;

	public OtherLoginResult(LoginEnum lenum, String email) {
		super();
		this.lenum = lenum;
		this.email = email;
	}

	public LoginEnum getLenum() {
		return lenum;
	}

	public String getEmail() {
		return email;
	}

}
