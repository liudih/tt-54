package events.interaction;

import java.util.Date;

public class ProductReviewEvent {

	final String listingID;

	final String email;

	final int rating;

	final String comments;

	final Date eventDate;

	public ProductReviewEvent(String listingID, String email, int rating,
			String comments, Date eventDate) {
		super();
		this.listingID = listingID;
		this.email = email;
		this.rating = rating;
		this.comments = comments;
		this.eventDate = eventDate;
	}

	public String getListingID() {
		return listingID;
	}

	public String getEmail() {
		return email;
	}

	public int getRating() {
		return rating;
	}

	public String getComments() {
		return comments;
	}

	public Date getEventDate() {
		return eventDate;
	}

}
