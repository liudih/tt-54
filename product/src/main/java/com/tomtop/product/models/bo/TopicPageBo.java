package com.tomtop.product.models.bo;

public class TopicPageBo extends BaseBo {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5378742738328011418L;
	
	private Integer id;
	private String type;
	private String title;
	private String htmlUrl;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getHtmlUrl() {
		return htmlUrl;
	}
	public void setHtmlUrl(String htmlUrl) {
		this.htmlUrl = htmlUrl;
	}
}
