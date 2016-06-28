package com.tomtop.product.models.bo;

import java.io.Serializable;

public class ProductImageBo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String imgUrl;
	private Boolean isSmall;//是否为小图
	private Boolean isMain;//是否为主图
	public String getImgUrl() {
		return imgUrl;
	}
	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}
	public Boolean getIsSmall() {
		return isSmall;
	}
	public void setIsSmall(Boolean isSmall) {
		this.isSmall = isSmall;
	}
	public Boolean getIsMain() {
		return isMain;
	}
	public void setIsMain(Boolean isMain) {
		this.isMain = isMain;
	}
}
