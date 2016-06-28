package valueobjects.interaction;

import java.io.Serializable;

public class ReviewCountAndScore implements Serializable  {
	private static final long serialVersionUID = 1L;
	private String listingId;
	private Integer reviewCount;
	private Double avgScore;

	public ReviewCountAndScore() {
	};

	public ReviewCountAndScore(Integer reviewCount, Double avgScore) {
		this(null, reviewCount, avgScore);
	};

	public ReviewCountAndScore(String listingid, Integer reviewCount,
			Double avgScore) {
		super();
		this.listingId = listingid;
		this.reviewCount = reviewCount;
		this.avgScore = avgScore;
	}

	public Double getAvgScore() {
		return avgScore != null ? avgScore : 0.0;
	}

	public String getListingId() {
		return listingId;
	}

	public Integer getReviewCount() {
		return reviewCount != null ? reviewCount : 0;
	}

	public void setListingId(String listingid) {
		this.listingId = listingid;
	}

	public void setReviewCount(Integer reviewCount) {
		this.reviewCount = reviewCount;
	}

	public void setAvgScore(Double avgScore) {
		this.avgScore = avgScore;
	}

	@Override
	public String toString() {
		return "ReviewCountAndScore [listingId=" + listingId + ", reviewCount="
				+ reviewCount + ", avgScore=" + avgScore + "]";
	}

}