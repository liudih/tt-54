package valuesobject.mobile.member;

import java.io.Serializable;
import java.util.Date;

public class SessionMember implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer id;

	private Date loginTime;

	private String email;

	public SessionMember() {
		super();
	}

	public SessionMember(Integer id, String email) {
		super();
		this.id = id;
		this.email = email;
		this.loginTime = new Date();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Date getLoginTime() {
		return loginTime;
	}

	public void setLoginTime(Date loginTime) {
		this.loginTime = loginTime;
	}
}
