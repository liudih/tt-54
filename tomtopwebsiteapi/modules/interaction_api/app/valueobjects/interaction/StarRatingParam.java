package valueobjects.interaction;

import java.io.Serializable;

public class StarRatingParam implements Serializable  {
	private static final long serialVersionUID = 1L;

	private Integer starsWidth;
	private Integer reviewCount;
	private String url;

	public StarRatingParam(Integer starsWidth, Integer reviewCount, String url) {
		super();
		this.starsWidth = starsWidth;
		this.reviewCount = reviewCount;
		this.url = url;
	}

	public Integer getStarsWidth() {
		return starsWidth;
	}

	public void setStarsWidth(Integer starsWidth) {
		this.starsWidth = starsWidth;
	}

	public Integer getReviewCount() {
		return reviewCount;
	}

	public void setReviewCount(Integer reviewCount) {
		this.reviewCount = reviewCount;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

}
