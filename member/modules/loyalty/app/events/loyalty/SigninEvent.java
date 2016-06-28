package events.loyalty;

public class SigninEvent {
	final int siteID;
	final String email;	

	public SigninEvent(int siteID, String email) {
		this.siteID = siteID;
		this.email = email;	
	}

	public int getSiteID() {
		return siteID;
	}

	public String getEmail() {
		return email;
	}	
}
