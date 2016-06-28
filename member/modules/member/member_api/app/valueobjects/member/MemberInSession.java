package valueobjects.member;

import java.io.Serializable;

public class MemberInSession implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 4167856139422745576L;

	int memberId;
	String username;
	String email;
	String sessionId;

	public MemberInSession() {
	}

	MemberInSession(int memberId, String username, String email,
			String sessionId) {
		this.memberId = memberId;
		this.username = username;
		this.sessionId = sessionId;
		this.email = email;
	}

	public static MemberInSession newInstance(int memberId, String username,
			String email, String sessionId) {
		return new MemberInSession(memberId, username, email, sessionId);
	}

	public int getMemberId() {
		return memberId;
	}

	public String getUsername() {
		return username;
	}

	public String getSessionId() {
		return sessionId;
	}

	public String getEmail() {
		return email;
	}

	@Override
	public String toString() {
		return "MemberInSession [memberId=" + memberId + ", username="
				+ username + ", email=" + email + ", sessionId=" + sessionId
				+ "]";
	}

}
