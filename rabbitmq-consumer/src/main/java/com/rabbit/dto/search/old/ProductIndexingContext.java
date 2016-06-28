package com.rabbit.dto.search.old;

import java.util.List;

import com.rabbit.dto.product.ProductCategoryMapper;
import com.rabbit.dto.product.ProductEntityMap;
import com.rabbit.dto.product.ProductLabel;
import com.rabbit.dto.product.ProductSalePrice;
import com.rabbit.dto.product.ProductStorageMap;
import com.rabbit.dto.valueobjects.price.Price;
import com.rabbit.dto.valueobjects.product.ProductBaseTranslate;


public class ProductIndexingContext {

	final int siteId;

	final String listingId;

	final ProductBaseTranslate product;

	final List<ProductCategoryMapper> categories;

	final List<ProductBaseTranslate> otherInfos;

	final List<ProductEntityMap> attributes;

	final List<ProductSalePrice> sales;

	final List<ProductLabel> tags;

	final Price price;

	final int viewCount;

	final List<String> relatedSku;

	final List<ProductStorageMap> storage;
	
	final List<RecommendDoc> categoryRecommend;

	public ProductIndexingContext(int siteId, String listingId,
			ProductBaseTranslate product,
			List<ProductCategoryMapper> categories,
			List<ProductBaseTranslate> infos,
			List<ProductEntityMap> attributes, List<ProductSalePrice> sales,
			List<ProductLabel> tags, Price price, int viewCount,
			List<String> relatedSku, List<ProductStorageMap> storage, 
			List<RecommendDoc> categoryRecommend) {
		super();
		this.siteId = siteId;
		this.listingId = listingId;
		this.product = product;
		this.otherInfos = infos;
		this.attributes = attributes;
		this.sales = sales;
		this.categories = categories;
		this.tags = tags;
		this.price = price;
		this.viewCount = viewCount;
		this.relatedSku = relatedSku;
		this.storage = storage;
		this.categoryRecommend = categoryRecommend;
	}

	public int getSiteId() {
		return siteId;
	}

	public String getListingId() {
		return listingId;
	}

	public ProductBaseTranslate getProduct() {
		return product;
	}

	public List<ProductBaseTranslate> getOtherInfos() {
		return otherInfos;
	}

	public List<ProductEntityMap> getAttributes() {
		return attributes;
	}

	public List<ProductSalePrice> getSales() {
		return sales;
	}

	public List<ProductCategoryMapper> getCategories() {
		return categories;
	}

	public Price getPrice() {
		return price;
	}

	public List<ProductLabel> getTags() {
		return tags;
	}

	public int getViewCount() {
		return viewCount;
	}

	public List<String> getRelatedSku() {
		return relatedSku;
	}

	public List<ProductStorageMap> getStorage() {
		return storage;
	}

	public List<RecommendDoc> getCategoryRecommend() {
		return categoryRecommend;
	}
}
