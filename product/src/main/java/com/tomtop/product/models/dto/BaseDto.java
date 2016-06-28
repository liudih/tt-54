package com.tomtop.product.models.dto;

import java.io.Serializable;
import java.util.Date;

/**
 * DTO 基类
 * 
 * @author 文龙
 *
 */
public class BaseDto implements Serializable {

	private static final long serialVersionUID = 1778833758971325281L;

	/**
	 * 商品唯一id
	 */
	String listingId;

	String sku;
	/**
	 * 站点
	 */
	private String websiteId;
	/**
	 * 语言id
	 */
	private String languageId;
	/**
	 * 创建人
	 */
	private String createUser;
	/**
	 * 创建时间
	 */
	private Date createTime;
	/**
	 * 最近修改人
	 */
	private String modifyUser;
	/**
	 * 最近修改时间
	 */
	private Date modifyTime;

	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	public String getListingId() {
		return listingId;
	}

	public void setListingId(String listingId) {
		this.listingId = listingId;
	}

	public String getWebsiteId() {
		return websiteId;
	}

	public void setWebsiteId(String websiteId) {
		this.websiteId = websiteId;
	}

	public String getLanguageId() {
		return languageId;
	}

	public void setLanguageId(String languageId) {
		this.languageId = languageId;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getCreateUser() {
		return createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	public Date getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}

	public String getModifyUser() {
		return modifyUser;
	}

	public void setModifyUser(String modifyUser) {
		this.modifyUser = modifyUser;
	}
}
