package com.rabbit.dto;

import java.io.Serializable;

public class ProductBundleSaleLite implements Serializable {

	private static final long serialVersionUID = 1L;
	private String listingId;
	private String title;
	private String url;
	private Double price;
	private Double salePrice;
	private Double discount;
	private String imgUrl;
	private Boolean isMain;
	private Integer priority;
	private String currency;
	private Double exchangeRate;
	private String symbol;
	private boolean isActivity;

	public Double getExchangeRate() {
		return exchangeRate;
	}

	public void setExchangeRate(Double exchangeRate) {
		this.exchangeRate = exchangeRate;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getListingId() {
		return listingId;
	}

	public void setListingId(String listingId) {
		this.listingId = listingId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Double getPrice() {
		return this.price;
		/*
		 * return new BigDecimal(price) .multiply(new
		 * BigDecimal(this.getExchangeRate())) .setScale(2,
		 * RoundingMode.HALF_UP).doubleValue();
		 */
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public Double getSalePrice() {
		/*
		 * if (salePrice == null) { return new BigDecimal(this.getPrice())
		 * .multiply(new BigDecimal(1).subtract(new BigDecimal(this.discount)))
		 * .setScale(2, RoundingMode.HALF_UP).doubleValue(); }
		 */
		return this.salePrice;
	}

	public void setSalePrice(Double salePrice) {
		this.salePrice = salePrice;
	}

	public Double getDiscount() {
		return discount;
	}

	public void setDiscount(Double discount) {
		this.discount = discount;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public Boolean getIsMain() {
		return isMain;
	}

	public void setIsMain(Boolean isMain) {
		this.isMain = isMain;
	}

	public Integer getPriority() {
		return priority;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public boolean isActivity() {
		return isActivity;
	}

	public void setActivity(boolean isActivity) {
		this.isActivity = isActivity;
	}

}
