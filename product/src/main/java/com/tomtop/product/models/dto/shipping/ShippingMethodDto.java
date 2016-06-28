package com.tomtop.product.models.dto.shipping;


import java.io.Serializable;
import java.util.List;

public class ShippingMethodDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer storageId;// 仓库ID
	private String country;// 国家代码
	private Double weight;// 总重量
	private Double shippingWeight;// 计算重量（减去免邮sku的重量）
	private boolean hasAllFreeshipping;// ~ 是否有全免邮产品
	private Integer lang;// 语言ID
	private Double grandTotal;// 总价格
	private List<String> listingIds;// listingID的list
	private Boolean isSpecial;// 是否包含特殊商品
	private String currency;// 货币代码
	private Integer websiteId;// 站点ID

	public ShippingMethodDto() {
	}

	public ShippingMethodDto(Integer storageId, String country,
			Double weight, Double shippingWeight, Integer lang,
			Double grandTotal, List<String> listingIds, Boolean isSpecial,
			String currency, int websiteId, boolean hasAllFreeshipping) {
		this.storageId = storageId;
		this.country = country;
		this.weight = weight;
		this.shippingWeight = shippingWeight;
		this.lang = lang;
		this.grandTotal = grandTotal;
		this.listingIds = listingIds;
		this.isSpecial = isSpecial;
		this.currency = currency;
		this.websiteId = websiteId;
		this.hasAllFreeshipping = hasAllFreeshipping;
	}

	public Integer getStorageId() {
		return storageId;
	}

	public void setStorageId(Integer storageId) {
		this.storageId = storageId;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public Double getWeight() {
		return weight;
	}

	public void setWeight(Double weight) {
		this.weight = weight;
	}

	public Double getShippingWeight() {
		return shippingWeight;
	}

	public void setShippingWeight(Double shippingWeight) {
		this.shippingWeight = shippingWeight;
	}

	public Integer getLang() {
		return lang;
	}

	public void setLang(Integer lang) {
		this.lang = lang;
	}

	public Double getGrandTotal() {
		return grandTotal;
	}

	public void setGrandTotal(Double grandTotal) {
		this.grandTotal = grandTotal;
	}

	public List<String> getListingIds() {
		return listingIds;
	}

	public void setListingIds(List<String> listingIds) {
		this.listingIds = listingIds;
	}

	public Boolean getIsSpecial() {
		return isSpecial;
	}

	public void setIsSpecial(Boolean isSpecial) {
		this.isSpecial = isSpecial;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public Integer getWebsiteId() {
		return websiteId;
	}

	public void setWebsiteId(Integer websiteId) {
		this.websiteId = websiteId;
	}

	public boolean isHasAllFreeshipping() {
		return hasAllFreeshipping;
	}

	public void setHasAllFreeshipping(boolean hasAllFreeshipping) {
		this.hasAllFreeshipping = hasAllFreeshipping;
	}

	@Override
	public String toString() {
		return "ShippingMethodRequst [storageId=" + storageId + ", country="
				+ country + ", weight=" + weight + ", shippingWeight="
				+ shippingWeight + ", lang=" + lang + ", grandTotal="
				+ grandTotal + ", listingIds=" + listingIds + ", isSpecial="
				+ isSpecial + ", currency=" + currency + ", websiteId="
				+ websiteId + "]";
	}
}
