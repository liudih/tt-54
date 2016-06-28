package valueobjects.livechat;

import java.util.Date;

public class Status {

	String ltc;
	String alias;
	Date lastSeen;
	Date loginAt;
	String topic;
	int iroleid;
	String ip;
	String country;
	int ilanguageid;

	public String getLtc() {
		return ltc;
	}

	public void setLtc(String ltc) {
		this.ltc = ltc;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public Date getLastSeen() {
		return lastSeen;
	}

	public void setLastSeen(Date lastSeen) {
		this.lastSeen = lastSeen;
	}

	public Date getLoginAt() {
		return loginAt;
	}

	public void setLoginAt(Date loginAt) {
		this.loginAt = loginAt;
	}

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public int getIroleid() {
		return iroleid;
	}

	public void setIroleid(int iroleid) {
		this.iroleid = iroleid;
	}

	public int getIlanguageid() {
		return ilanguageid;
	}

	public void setIlanguageid(int ilanguageid) {
		this.ilanguageid = ilanguageid;
	}

	@Override
	public String toString() {
		return "Status [ltc=" + ltc + ", alias=" + alias + ", lastSeen="
				+ lastSeen + ", loginAt=" + loginAt + ", topic=" + topic
				+ ", iroleid=" + iroleid + ", ip=" + ip + ", country="
				+ country + ", ilanguageid=" + ilanguageid + "]";
	}

}
