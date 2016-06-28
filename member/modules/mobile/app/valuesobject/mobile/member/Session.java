package valuesobject.mobile.member;

import java.io.Serializable;

public class Session implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public final static String M_SESSION_NAME = "M_SESSION";

	public final static String AUTH_SESSION_NAME = "AUTH_SESSION";

	private String sessionID;

	private MobileContext mobileContext;

	private MobileLoginContext loginContext;

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

	public MobileLoginContext getLoginContext() {
		return loginContext;
	}

	public void setLoginContext(MobileLoginContext loginContext) {
		this.loginContext = loginContext;
	}

}
