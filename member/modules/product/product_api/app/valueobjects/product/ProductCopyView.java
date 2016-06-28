package valueobjects.product;

import java.util.List;
import java.util.Map;

import valueobjects.base.Page;
import valueobjects.price.Price;
import dto.ProductAttributeItem;
import dto.product.ProductActivityRelation;

public class ProductCopyView {

	final Page<ProductActivityRelation> activityPage;
	final Map<String, String> parentImgurl;
	final Map<String, String> imgurl;
	final Map<String, String> parentTitle;
	final Map<String, String> title;
	final Map<String, String> parentDes;
	final Map<String, String> des;
	final Map<String, Price> price;
	final Map<String, Integer> parentStar;
	final Map<String, Integer> star;
	final Map<String, Integer> parentCount;
	final Map<String, Integer> count;
	final Map<String, String> parentUrl;
	final Map<String, String> subUrl;
	final Map<String, List<ProductAttributeItem>> parentMap;
	final Map<String, List<ProductAttributeItem>> subMap;
	final Map<String, String> outOfDate;
	final Map<String, Double> oldpPrice;
	final Map<String, Double> oldPrice;

	public ProductCopyView(Page<ProductActivityRelation> activityPage,
			Map<String, String> parentImgurl, Map<String, String> imgurl,
			Map<String, String> parentTitle, Map<String, String> title,
			Map<String, String> parentDes, Map<String, String> des,
			Map<String, Price> price, Map<String, Integer> parentStar,
			Map<String, Integer> star, Map<String, Integer> parentCount,
			Map<String, Integer> count, Map<String, String> parentUrl,
			Map<String, String> subUrl,
			Map<String, List<ProductAttributeItem>> parentMap,
			Map<String, List<ProductAttributeItem>> subMap,
			Map<String, String> outOfDate, Map<String, Double> oldpPrice,
			Map<String, Double> oldPrice) {
		super();
		this.activityPage = activityPage;
		this.parentImgurl = parentImgurl;
		this.imgurl = imgurl;
		this.parentTitle = parentTitle;
		this.title = title;
		this.parentDes = parentDes;
		this.des = des;
		this.price = price;
		this.parentStar = parentStar;
		this.star = star;
		this.parentCount = parentCount;
		this.count = count;
		this.parentUrl = parentUrl;
		this.subUrl = subUrl;
		this.parentMap = parentMap;
		this.subMap = subMap;
		this.outOfDate = outOfDate;
		this.oldpPrice = oldpPrice;
		this.oldPrice = oldPrice;
	}

	public Page<ProductActivityRelation> getActivityPage() {
		return activityPage;
	}

	public Map<String, String> getParentImgurl() {
		return parentImgurl;
	}

	public Map<String, String> getImgurl() {
		return imgurl;
	}

	public Map<String, String> getParentTitle() {
		return parentTitle;
	}

	public Map<String, String> getTitle() {
		return title;
	}

	public Map<String, String> getParentDes() {
		return parentDes;
	}

	public Map<String, String> getDes() {
		return des;
	}

	public Map<String, Price> getPrice() {
		return price;
	}

	public Map<String, Integer> getParentStar() {
		return parentStar;
	}

	public Map<String, Integer> getStar() {
		return star;
	}

	public Map<String, Integer> getParentCount() {
		return parentCount;
	}

	public Map<String, Integer> getCount() {
		return count;
	}

	public Map<String, String> getParentUrl() {
		return parentUrl;
	}

	public Map<String, String> getSubUrl() {
		return subUrl;
	}

	public Map<String, List<ProductAttributeItem>> getParentMap() {
		return parentMap;
	}

	public Map<String, List<ProductAttributeItem>> getSubMap() {
		return subMap;
	}

	public Map<String, String> getOutOfDate() {
		return outOfDate;
	}

	public Map<String, Double> getOldpPrice() {
		return oldpPrice;
	}

	public Map<String, Double> getOldPrice() {
		return oldPrice;
	}

}
