package valuesobject.mobile.member;

import java.io.Serializable;

public class MobileLoginContext implements Serializable {

	private static final long serialVersionUID = 1L;

	private boolean login;

	private boolean captchas;

	private int reqCount;

	private Serializable payload;

	public MobileLoginContext() {
		super();
	}

	public MobileLoginContext(boolean login, boolean captchas, int reqCount) {
		super();
		this.login = login;
		this.captchas = captchas;
		this.reqCount = reqCount;
	}

	public boolean isLogin() {
		return login;
	}

	public void setLogin(boolean login) {
		this.login = login;
	}

	public boolean isCaptchas() {
		return captchas;
	}

	public void setCaptchas(boolean captchas) {
		this.captchas = captchas;
	}

	public int getReqCount() {
		return reqCount;
	}

	public void setReqCount(int reqCount) {
		this.reqCount = reqCount;

		if (this.reqCount >= 3) {
			this.setCaptchas(true);
		}
	}

	public Serializable getPayload() {
		return payload;
	}

	public void setPayload(Serializable payload) {
		this.payload = payload;
	}

}
