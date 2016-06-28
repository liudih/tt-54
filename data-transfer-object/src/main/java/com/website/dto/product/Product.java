package com.website.dto.product;

import java.util.Date;
import java.util.List;

public class Product {

	private String listingId;

	private Integer websiteId;

	private String sku;

	private Integer status;

	/**
	 * 新品开始时间
	 */
	private Date newFromDate;

	/**
	 * 新品结束时间
	 */
	private Date newToDate;

	/**
	 * 是否特殊产品
	 */
	private Boolean special;

	private Integer qty;

	private Double price;
	/**
	 * 成本
	 */
	private Double cost;

	private Double weight;

	/**
	 * 是否清货产品
	 */
	private Boolean cleanrstocks;

	/**
	 * 是否特色商品
	 */
	private Boolean featured;

	private Boolean isNew;

	/**
	 * 热卖品
	 */
	private Boolean hot;

	/**
	 * 是否可见
	 */
	private Boolean visible;

	/**
	 * 品类id
	 */
	private List<Integer> categoryIds;

	/**
	 * 产品详情
	 */
	private List<TranslateItem> items;

	private List<ImageItem> images;

	private List<VideoItem> videos;

	private List<AttributeItem> attributes;

	private List<Integer> storages;

	private Double freight;

	public Integer getWebsiteId() {
		return websiteId;
	}

	/**
	 * require
	 * 
	 * @param websiteId
	 */
	public void setWebsiteId(Integer websiteId) {
		this.websiteId = websiteId;
	}

	public String getSku() {
		return sku;
	}

	/**
	 * require
	 * 
	 * @param sku
	 */
	public void setSku(String sku) {
		this.sku = sku;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Date getNewFromDate() {
		return newFromDate;
	}

	public void setNewFromDate(Date newFromDate) {
		this.newFromDate = newFromDate;
	}

	public Date getNewToDate() {
		return newToDate;
	}

	public void setNewToDate(Date newToDate) {
		this.newToDate = newToDate;
	}

	public Boolean getSpecial() {
		return special;
	}

	public void setSpecial(Boolean special) {
		this.special = special;
	}

	public Integer getQty() {
		return qty;
	}

	/**
	 * 大于0
	 * 
	 * @param qty
	 */
	public void setQty(Integer qty) {
		this.qty = qty;
	}

	public Double getPrice() {
		return price;
	}

	/**
	 * 大于0
	 * 
	 * @param price
	 */
	public void setPrice(Double price) {
		this.price = price;
	}

	public Double getWeight() {
		return weight;
	}

	/**
	 * 大于0
	 * 
	 * @param weight
	 */
	public void setWeight(Double weight) {
		this.weight = weight;
	}

	public Boolean getCleanrstocks() {
		return cleanrstocks;
	}

	public void setCleanrstocks(Boolean cleanrstocks) {
		this.cleanrstocks = cleanrstocks;
	}

	public Boolean getFeatured() {
		return featured;
	}

	public void setFeatured(Boolean featured) {
		this.featured = featured;
	}

	public Boolean getIsNew() {
		return isNew;
	}

	public void setIsNew(Boolean isNew) {
		this.isNew = isNew;
	}

	public Boolean getHot() {
		return hot;
	}

	public void setHot(Boolean hot) {
		this.hot = hot;
	}

	public Boolean getVisible() {
		return visible;
	}

	public void setVisible(Boolean visible) {
		this.visible = visible;
	}

	public List<Integer> getCategoryIds() {
		return categoryIds;
	}

	/**
	 * require
	 * 
	 * @param categoryIds
	 */
	public void setCategoryIds(List<Integer> categoryIds) {
		this.categoryIds = categoryIds;
	}

	public List<TranslateItem> getItems() {
		return items;
	}

	public void setItems(List<TranslateItem> items) {
		this.items = items;
	}

	public List<ImageItem> getImages() {
		return images;
	}

	public void setImages(List<ImageItem> images) {
		this.images = images;
	}

	public List<VideoItem> getVideos() {
		return videos;
	}

	public void setVideos(List<VideoItem> videos) {
		this.videos = videos;
	}

	public List<AttributeItem> getAttributes() {
		return attributes;
	}

	public void setAttributes(List<AttributeItem> attributes) {
		this.attributes = attributes;
	}

	public List<Integer> getStorages() {
		return storages;
	}

	/**
	 * require
	 * 
	 * @param storages
	 */
	public void setStorages(List<Integer> storages) {
		this.storages = storages;
	}

	public Double getCost() {
		return cost;
	}

	public void setCost(Double cost) {
		this.cost = cost;
	}

	public Double getFreight() {
		return freight;
	}

	public void setFreight(Double freight) {
		this.freight = freight;
	}

	public String getListingId() {
		return listingId;
	}

	public void setListingId(String listingId) {
		this.listingId = listingId;
	}

}
