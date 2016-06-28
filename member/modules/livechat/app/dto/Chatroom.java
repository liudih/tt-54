package dto;

public class Chatroom {
	String sessionid;
	String otherAlias;
	String ip;
	String country;
	
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
	public String getSessionid() {
		return sessionid;
	}
	public void setSessionid(String sessionid) {
		this.sessionid = sessionid;
	}
	public String getOtherAlias() {
		return otherAlias;
	}
	public void setOtherAlias(String otherAlias) {
		this.otherAlias = otherAlias;
	}
}
