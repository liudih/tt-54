package com.tomtop.product.models.bo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class ProductReviewBo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4084570822517917561L;
	private String email;
	private Double overall;
	private Integer usefulness;
	private Integer shipping;
	private Integer price;
	private Integer quality;
	private String commentDate; // 评论日期
	private String comment;// 评论
	private List<String> imgUrls;// 评论图片集合
	private List<String> videos;// 评论视频集合

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Double getOverall() {
		return overall;
	}

	public void setOverall(Double overall) {
		this.overall = overall;
	}

	public Integer getUsefulness() {
		return usefulness;
	}

	public void setUsefulness(Integer usefulness) {
		this.usefulness = usefulness;
	}

	public Integer getShipping() {
		return shipping;
	}

	public void setShipping(Integer shipping) {
		this.shipping = shipping;
	}

	public Integer getPrice() {
		return price;
	}

	public void setPrice(Integer price) {
		this.price = price;
	}

	public Integer getQuality() {
		return quality;
	}

	public void setQuality(Integer quality) {
		this.quality = quality;
	}

	public String getCommentDate() {
		return commentDate;
	}

	public void setCommentDate(String commentDate) {
		this.commentDate = commentDate;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public List<String> getImgUrls() {
		return imgUrls;
	}

	public void setImgUrls(List<String> imgUrls) {
		this.imgUrls = imgUrls;
	}

	public List<String> getVideos() {
		return videos;
	}

	public void setVideos(List<String> videos) {
		this.videos = videos;
	}
}
