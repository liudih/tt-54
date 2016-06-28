package events.loyalty;

public class CommentEvent extends ReviewEvent{
	public CommentEvent(String listingid, int siteID, String email) {
		super(listingid, siteID, email);
	}
}
