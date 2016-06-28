package valueobjects.product;

import java.util.List;

import valueobjects.price.Price;
import valueobjects.product.index.RecommendDoc;
import dto.product.ProductCategoryMapper;
import dto.product.ProductEntityMap;
import dto.product.ProductLabel;
import dto.product.ProductSalePrice;
import dto.product.ProductStorageMap;

public class ProductIndexingContextExtra {
	int siteId;
	String listingId;
	ProductBaseTranslate product;
	List<ProductCategoryMapper> categories;
	List<ProductBaseTranslate> otherInfos;
	List<ProductEntityMap> attributes;
	List<ProductSalePrice> sales;
	List<ProductLabel> tags;
	PriceExtra price;
	int viewCount;
	List<String> relatedSku;
	List<ProductStorageMap> storage;
	List<RecommendDocExtra> categoryRecommend;

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

	public PriceExtra getPrice() {
		return price;
	}

	public void setPrice(PriceExtra price) {
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

	public List<RecommendDocExtra> getCategoryRecommend() {
		return categoryRecommend;
	}

	public void setCategoryRecommend(List<RecommendDocExtra> categoryRecommend) {
		this.categoryRecommend = categoryRecommend;
	}

}
