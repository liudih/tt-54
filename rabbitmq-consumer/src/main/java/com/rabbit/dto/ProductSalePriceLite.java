package com.rabbit.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class ProductSalePriceLite implements Serializable {

	private static final long serialVersionUID = 1L;

	private String listingId;

	private Double salePrice;

	private Date beginDate;

	private Date endDate;

	private Double price;

	public String getListingId() {
		return listingId;
	}

	public void setListingId(String listingId) {
		this.listingId = listingId;
	}

	public Double getSalePrice() {
		return salePrice;
	}

	public void setSalePrice(Double salePrice) {
		this.salePrice = salePrice;
	}

	public Date getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public int getDiscount() {
		BigDecimal price = new BigDecimal(this.getPrice());
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
}
