package valuesobject.mobile.member;

import java.io.Serializable;

public class Session implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public final static String M_SESSION_NAME = "M_SESSION";

	public final static String LOGINT_CONTEXT = "LOGIN_CONTEXT";

	public final static String M_LOGIN_CONTEXT = "M_LOGIN_CONTEXT";

	private String sessionID;

	private MobileContext mobileContext;

	public String getSessionID() {
		return sessionID;
	}

	public void setSessionID(String sessionID) {
		this.sessionID = sessionID;
	}

	public MobileContext getMobileContext() {
		return mobileContext;
	}

	public void setMobileContext(MobileContext mobileContext) {
		this.mobileContext = mobileContext;
	}

}
