package forms.manager.login;


public class PublicLoginForm {
	
	String username;
	String jobNumber;
	String sysName;
	String timestamp;
	String signData;
	String seId;
	
	public String getSeId() {
		return seId;
	}
	public void setSeId(String seId) {
		this.seId = seId;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getJobNumber() {
		return jobNumber;
	}
	public void setJobNumber(String jobNumber) {
		this.jobNumber = jobNumber;
	}
	public String getSysName() {
		return sysName;
	}
	public void setSysName(String sysName) {
		this.sysName = sysName;
	}
	public String getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	public String getSignData() {
		return signData;
	}
	public void setSignData(String signData) {
		this.signData = signData;
	}
	@Override
	public String toString() {
		return "PublicLoginForm [username=" + username + ", jobNumber="
				+ jobNumber + ", sysName=" + sysName + ", timestamp="
				+ timestamp + ", signData=" + signData + ", seId=" + seId + "]";
	}
	
	
}
