package com.rabbit.dto.valueobjects.product;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class ProductLite implements Serializable {
	private static final long serialVersionUID = 1L;
	private String title;
	private String url; // 路由地址
	private Double salePrice; // 促销价格
	private String imageUrl; // 图片地址
	private String listingId;
	@SuppressWarnings("unused")
	private Integer reducePercent; // 降价百分比
	private Double originalPrice; // 原价
	private Integer rank; // 各种排名
	private Integer type; // 保留
	private Date endDate; // 促销截止时间（倒计时用）
	private String sku;
	private Double weight;
	private Integer istatus;	//产品状态

	public ProductLite() {
	};

	public ProductLite(String title, String url, Double price, String imageUrl,
			Double score, Integer reviews) {
		super();
		this.title = title;
		this.url = url;
		this.salePrice = price;
		this.imageUrl = imageUrl;
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

	public Double getSalePrice() {
		return salePrice;
	}

	public void setSalePrice(Double salePrice) {
		this.salePrice = salePrice;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getListingId() {
		return listingId;
	}

	public void setListingId(String listingId) {
		this.listingId = listingId;
	}

	public int getReducePercent() {
		BigDecimal price = new BigDecimal(this.getOriginalPrice());
		BigDecimal discountprice = price.subtract(new BigDecimal(this
				.getSalePrice()));
		return discountprice.divide(price, 2, BigDecimal.ROUND_HALF_UP)
				.multiply(new BigDecimal(100)).intValue();
	}

	public void setReducePercent(Integer reducePercent) {
		this.reducePercent = reducePercent;
	}

	public Double getOriginalPrice() {
		return originalPrice;
	}

	public void setOriginalPrice(Double originalPrice) {
		this.originalPrice = originalPrice;
	}

	public Integer getRank() {
		return rank;
	}

	public void setRank(Integer rank) {
		this.rank = rank;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	public Double getWeight() {
		return weight;
	}

	public void setWeight(Double weight) {
		this.weight = weight;
	}

	public int getDiscount() {
		BigDecimal price = new BigDecimal(this.getOriginalPrice());
		BigDecimal discountprice = price.subtract(new BigDecimal(this
				.getSalePrice()));
		return discountprice.divide(price, 2, BigDecimal.ROUND_HALF_UP)
				.multiply(new BigDecimal(100)).intValue();
	}

	public long getEndSeconds() {
		long second = this.getEndDate().getTime() - new Date().getTime();
		if (second <= 0) {
			return 0;
		}
		return (second / 1000);
	}

	public Integer getIstatus() {
		return istatus;
	}

	public void setIstatus(Integer istatus) {
		this.istatus = istatus;
	}
}
