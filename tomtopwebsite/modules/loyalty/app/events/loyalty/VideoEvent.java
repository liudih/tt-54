package events.loyalty;

public class VideoEvent extends ReviewEvent{
	public VideoEvent(String listingid, int siteID, String email) {
		super(listingid, siteID, email);
	}
}
