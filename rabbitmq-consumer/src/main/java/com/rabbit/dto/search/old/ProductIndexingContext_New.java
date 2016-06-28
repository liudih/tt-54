package com.rabbit.dto.search.old;

import java.util.List;

import com.rabbit.dto.product.ProductCategoryMapper;
import com.rabbit.dto.product.ProductEntityMap;
import com.rabbit.dto.product.ProductLabel;
import com.rabbit.dto.product.ProductSalePrice;
import com.rabbit.dto.product.ProductStorageMap;
import com.rabbit.dto.valueobjects.price.Price;
import com.rabbit.dto.valueobjects.product.ProductBaseTranslate;

public class ProductIndexingContext_New {

	int siteId;

	String listingId;

	ProductBaseTranslate product;

	List<ProductCategoryMapper> categories;

	List<ProductBaseTranslate> otherInfos;

	List<ProductEntityMap> attributes;

	List<ProductSalePrice> sales;

	List<ProductLabel> tags;

	Price price;

	int viewCount;

	List<String> relatedSku;

	List<ProductStorageMap> storage;

	List<RecommendDoc> categoryRecommend;

	public int getSiteId() {
		return siteId;
	}

	public void setSiteId(int siteId) {
		this.siteId = siteId;
	}

	public String getListingId() {
		return listingId;
	}

	public void setListingId(String listingId) {
		this.listingId = listingId;
	}

	public ProductBaseTranslate getProduct() {
		return product;
	}

	public void setProduct(ProductBaseTranslate product) {
		this.product = product;
	}

	public List<ProductCategoryMapper> getCategories() {
		return categories;
	}

	public void setCategories(List<ProductCategoryMapper> categories) {
		this.categories = categories;
	}

	public List<ProductBaseTranslate> getOtherInfos() {
		return otherInfos;
	}

	public void setOtherInfos(List<ProductBaseTranslate> otherInfos) {
		this.otherInfos = otherInfos;
	}

	public List<ProductEntityMap> getAttributes() {
		return attributes;
	}

	public void setAttributes(List<ProductEntityMap> attributes) {
		this.attributes = attributes;
	}

	public List<ProductSalePrice> getSales() {
		return sales;
	}

	public void setSales(List<ProductSalePrice> sales) {
		this.sales = sales;
	}

	public List<ProductLabel> getTags() {
		return tags;
	}

	public void setTags(List<ProductLabel> tags) {
		this.tags = tags;
	}

	public Price getPrice() {
		return price;
	}

	public void setPrice(Price price) {
		this.price = price;
	}

	public int getViewCount() {
		return viewCount;
	}

	public void setViewCount(int viewCount) {
		this.viewCount = viewCount;
	}

	public List<String> getRelatedSku() {
		return relatedSku;
	}

	public void setRelatedSku(List<String> relatedSku) {
		this.relatedSku = relatedSku;
	}

	public List<ProductStorageMap> getStorage() {
		return storage;
	}

	public void setStorage(List<ProductStorageMap> storage) {
		this.storage = storage;
	}

	public List<RecommendDoc> getCategoryRecommend() {
		return categoryRecommend;
	}

	public void setCategoryRecommend(List<RecommendDoc> categoryRecommend) {
		this.categoryRecommend = categoryRecommend;
	}

}
