package valueobjects.member;

import java.io.Serializable;

public class MemberRegistration implements Serializable {

	private static final long serialVersionUID = 1L;

	final String email;
	final String password;
	final String country;
	final boolean receiveNewsletter;
	String firstname;
	String lastname;

	public MemberRegistration(String email, String password, String country,
			boolean receiveNewsletter) {
		super();
		if (email == null) {
			throw new IllegalArgumentException(
					"MemberRegistration: Email is Empty");
		}
		this.email = email;
		this.password = password;
		this.country = country;
		this.receiveNewsletter = receiveNewsletter;
	}
	
	public MemberRegistration(String email, String password, String country,
			boolean receiveNewsletter, String firstname, String lastname) {
		super();
		this.email = email;
		this.password = password;
		this.country = country;
		this.receiveNewsletter = receiveNewsletter;
		this.firstname = firstname;
		this.lastname = lastname;
	}

	public String getEmail() {
		return email;
	}

	public String getPassword() {
		return password;
	}

	public String getCountry() {
		return country;
	}

	public boolean isReceiveNewsletter() {
		return receiveNewsletter;
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
