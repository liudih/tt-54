package com.tomtop.es.entity;

import java.io.Serializable;

/**
 * 产品数据格式
 * 
 * @author ztiny
 * @Date 2015-12-25
 */
public class ProductImageEntity implements Serializable {

	private static final long serialVersionUID = 2502281211062701703L;

	/**
	 * url
	 */
	private String url;
	/**
	 * 基础图
	 */
	private Boolean isBase;
	/**
	 * 排序
	 */
	private Integer order;
	/**
	 * 小图
	 */
	private Boolean isSmall;

	public ProductImageEntity(String url, Integer order, Boolean isBase, Boolean isSmall) {
		this.url = url;
		this.isBase = isBase;
		this.order = order;
		this.isSmall = isSmall;
	}

	public ProductImageEntity() {

	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Boolean getIsBase() {
		return isBase;
	}

	public void setIsBase(Boolean isBase) {
		this.isBase = isBase;
	}

	public Integer getOrder() {
		return order;
	}

	public void setOrder(Integer order) {
		this.order = order;
	}

	public Boolean getIsSmall() {
		return isSmall;
	}

	public void setIsSmall(Boolean isSmall) {
		this.isSmall = isSmall;
	}

}
