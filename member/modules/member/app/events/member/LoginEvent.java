package events.member;

import java.util.Date;

public class LoginEvent {

	final String ltc;
	final String stc;
	final String email;
	final int siteID;
	final String clientIP;
	final Date timestamp;
	final String device;

	public LoginEvent(String ltc, String stc, String clientIP, int siteID,
			String email, String device) {
		this.ltc = ltc;
		this.stc = stc;
		this.siteID = siteID;
		this.clientIP = clientIP;
		this.email = email;
		this.timestamp = new Date();
		this.device = device;
	}

	public String getLTC() {
		return ltc;
	}

	public String getSTC() {
		return stc;
	}

	public String getEmail() {
		return email;
	}

	public int getSiteID() {
		return siteID;
	}

	public String getClientIP() {
		return clientIP;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public String getDevice() {
		return device;
	}

}
