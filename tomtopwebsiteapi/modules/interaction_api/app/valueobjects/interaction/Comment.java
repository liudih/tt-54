package valueobjects.interaction;

import java.io.Serializable;
import java.util.List;

import valueobjects.product.IProductFragment;
import dto.ReviewsCountList;
import dto.interaction.Foverallrating;

public class Comment implements IProductFragment,Serializable {
	private static final long serialVersionUID = 1L;

	final List<ReviewsCountList> commentList;
	final Double averageScore;
	final Integer reviewCount;
	final Integer averageScoreStarWidth;
	final String listingID;
	final List<Foverallrating> foverallratings;

	public Comment(String listingID,
			List<ReviewsCountList> rlist,
			Double averageScore,
			Integer reviewCount,
			Integer averageScoreStarWidth,
			List<Foverallrating> foverallratings) {
		this.listingID = listingID;
		this.commentList = rlist;
		this.averageScore = averageScore;
		this.reviewCount = reviewCount;
		this.averageScoreStarWidth = averageScoreStarWidth;
		this.foverallratings = foverallratings;
	}

	public Double getAverageScore() {
		return averageScore;
	}

	public Integer getReviewCount() {
		return reviewCount;
	}

	public Integer getStarsWidth() {
		return averageScoreStarWidth;
	}

	public String getListingID() {
		return listingID;
	}

	public List<Foverallrating> getFoverallratings() {
		return foverallratings;
	}
	public List<ReviewsCountList> getCommentList() {
		return commentList;
	}
	public Integer getAverageScoreStarWidth() {
		return averageScoreStarWidth;
	}
}
