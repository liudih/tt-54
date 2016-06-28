package events.loyalty;

public class PhotoEvent extends ReviewEvent{
	public PhotoEvent(String listingid, int siteID, String email) {
		super(listingid, siteID, email);
	}
}
