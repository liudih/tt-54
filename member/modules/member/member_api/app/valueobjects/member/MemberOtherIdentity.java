package valueobjects.member;

import java.io.Serializable;

public class MemberOtherIdentity implements Serializable{

	private static final long serialVersionUID = 1L;
	
	final String source;
	final String id;
	final String email;

	public MemberOtherIdentity(String source, String id, String email) {
		super();
		this.source = source;
		this.id = id;
		this.email = email;
	}

	public String getSource() {
		return source;
	}

	public String getId() {
		return id;
	}

	public String getEmail() {
		return email;
	}

}
