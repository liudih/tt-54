package valueobjects.dodocool.interaction.product;

import java.util.Map;

import valueobjects.base.Page;
import valueobjects.product.IProductFragment;
import dto.ReviewsCountList;

public class InteractionComment implements IProductFragment {

	final String listingId;
	final Page<ReviewsCountList> reviewsPage;
	final Map<String, String> emailAndNamMap;

	public InteractionComment(String listingId,
			Page<ReviewsCountList> reviewsPage,
			Map<String, String> emailAndNamMap) {
		this.listingId = listingId;
		this.reviewsPage = reviewsPage;
		this.emailAndNamMap = emailAndNamMap;
	}

	public String getListingId() {
		return listingId;
	}

	public Map<String, String> getEmailAndNamMap() {
		return emailAndNamMap;
	}

	public Page<ReviewsCountList> getReviewsPage() {
		return reviewsPage;
	}

}
