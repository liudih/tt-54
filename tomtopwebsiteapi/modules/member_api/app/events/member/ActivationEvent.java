package events.member;

import java.util.Date;

public class ActivationEvent {

	final String ltc;
	final String stc;
	final String clientIP;
	final int siteID;
	final String email;
	final Date timestamp;
	// 语言
	final Integer language;

	public ActivationEvent(String ltc, String stc, String clientIP, int siteID,
			String email, Integer language) {
		super();
		this.ltc = ltc;
		this.stc = stc;
		this.clientIP = clientIP;
		this.siteID = siteID;
		this.email = email;
		this.timestamp = new Date();
		this.language = language;
	}

	public String getLtc() {
		return ltc;
	}

	public String getStc() {
		return stc;
	}

	public String getClientIP() {
		return clientIP;
	}

	public int getSiteID() {
		return siteID;
	}

	public String getEmail() {
		return email;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public Integer getLanguage() {
		return language;
	}

}
