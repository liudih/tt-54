package events.loyalty;

public class ReviewEvent {
	final String listingid;
	final int siteID;
	final String email;
	public ReviewEvent(String listingid, int siteID, String email) {
		this.listingid = listingid;
		this.siteID = siteID;
		this.email = email;
	}
	public String getListingid() {
		return listingid;
	}
	public int getSiteID() {
		return siteID;
	}
	public String getEmail() {
		return email;
	}
}
