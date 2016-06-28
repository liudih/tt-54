package com.tomtop.product.models.vo;

import java.io.Serializable;
import java.util.List;

public class HomeNewestVo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1039530041470737874L;
	private List<HomeNewestImageVo> image;
	private List<HomeNewestReviewVo> review;
	private List<HomeNewestVideoVo> video;
	
	public List<HomeNewestImageVo> getImage() {
		return image;
	}
	public void setImage(List<HomeNewestImageVo> image) {
		this.image = image;
	}
	public List<HomeNewestReviewVo> getReview() {
		return review;
	}
	public void setReview(List<HomeNewestReviewVo> review) {
		this.review = review;
	}
	public List<HomeNewestVideoVo> getVideo() {
		return video;
	}
	public void setVideo(List<HomeNewestVideoVo> video) {
		this.video = video;
	}
	
}
