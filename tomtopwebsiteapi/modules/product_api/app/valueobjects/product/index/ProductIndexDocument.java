package valueobjects.product.index;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.elasticsearch.common.collect.Lists;

import play.Logger;
import valueobjects.product.ProductBaseTranslate;
import valueobjects.search.LanguageSpecificInfo;
import valueobjects.search.ProductIndexingContext;
import valueobjects.search.ProductTypeInfo;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;

import dto.product.ProductLabel;

/**
 *
 * <ul>
 * <li>category</li>
 * <li>title</li>
 * <li>description</li>
 * <li>attributes</li>
 * <li>selling points</li>
 * <li>price / discount (for sorting, filtering)</li>
 * <li>new product from / to date</li>
 * </ul>
 * 
 * @author kmtong
 *
 */
public class ProductIndexDocument {

	/**
	 * Including all parent categories.
	 */
	List<Integer> categories;

	int site;

	@MappingType(type = "string", index = "not_analyzed")
	String sku;

	Map<Integer, LanguageSpecificInfo> infos;

	Map<String, ProductTypeInfo> tagsType;

	int viewCount;

	double weight;

	Date createdate;

	@MappingType(type = "string", index = "not_analyzed")
	List<String> attributes;

	@ExtraMappingExtension
	Map<String, ObjectNode> extras;

	@ChildMappingType(type = PriceDoc.class)
	PriceDoc price;

	@MappingType(type = "string", index = "not_analyzed")
	List<String> tags;

	int status;

	@MappingType(type = "string", index = "not_analyzed")
	String spu;

	boolean visible;

	@MappingType(type = "string", index = "not_analyzed")
	List<String> relatedSku;

	List<Integer> storagid;

	List<Date> tagDate;
	
	List<RecommendDoc> categoryRecommend;	//推荐排序

	public ProductIndexDocument(ProductIndexingContext context) {
		Logger.debug("listingid" + context.getListingId());
		this.site = context.getProduct().getIwebsiteid();
		this.sku = toLowerCase(context.getProduct().getCsku());
		this.categories = context.getCategories() != null ? Lists.transform(
				context.getCategories(), c -> c.getIcategory()) : null;
		this.attributes = Lists.transform(context.getAttributes(),
				am -> am.getIkey() + ":" + am.getIvalue());
		this.createdate = context.getProduct().getDcreatedate();
		this.price = context.getPrice() != null ? new PriceDoc(
				context.getPrice(), context.getSales()) : null;
		this.weight = context.getProduct().getFweight();
		this.viewCount = context.getViewCount();
		this.tags = Lists.transform(context.getTags(), t -> t.getCtype());
		Map<Integer, ProductBaseTranslate> translates = Maps.uniqueIndex(
				context.getOtherInfos(), t -> t.getIlanguageid());
		this.infos = Maps.transformValues(translates,
				i -> new LanguageSpecificInfo(i.getCtitle()));
		this.status = context.getProduct().getIstatus();
		this.spu = toLowerCase(context.getProduct().getCparentsku());
		this.visible = context.getProduct().isBvisible();
		this.relatedSku = context.getRelatedSku();
		this.storagid = context.getStorage() != null ? Lists.transform(
				context.getStorage(), s -> s.getIstorageid()) : null;
		this.tagDate = Lists.transform(context.getTags(),
				t -> t.getDcreatedate());
		List<ProductTypeInfo> transform = Lists.transform(
				context.getTags(),
				a -> {
					ProductTypeInfo productTypeInfo = new ProductTypeInfo(a
							.getCtype(), a.getDcreatedate());
					return productTypeInfo;
				});
		Map<String, ProductTypeInfo> collect = transform.stream().collect(
				Collectors.toMap(a -> a.getType(), a -> a));
		Multimap<String, Date> labelAndDateMap = HashMultimap.create();
		for (ProductLabel productLabel : context.getTags()) {

			String type = productLabel.getCtype();
			Date date = productLabel.getDcreatedate();

			if (type != null && type.length() > 0 && date != null) {
				labelAndDateMap.put(type, date);
			}
		}
		this.tagsType = collect.size() > 0 ? collect : null;
		this.categoryRecommend = context.getCategoryRecommend();
	}

	public List<Integer> getCategories() {
		return categories;
	}

	public void setCategories(List<Integer> categories) {
		this.categories = categories;
	}

	public int getSite() {
		return site;
	}

	public void setSite(int site) {
		this.site = site;
	}

	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	public int getViewCount() {
		return viewCount;
	}

	public void setViewCount(int viewCount) {
		this.viewCount = viewCount;
	}

	public PriceDoc getPrice() {
		return price;
	}

	public void setPrice(PriceDoc price) {
		this.price = price;
	}

	public List<String> getTags() {
		return tags;
	}

	public void setTags(List<String> tags) {
		this.tags = tags;
	}

	public Date getCreatedate() {
		return createdate;
	}

	public void setCreatedate(Date dcreatedate) {
		this.createdate = dcreatedate;
	}

	public List<String> getAttributes() {
		return attributes;
	}

	public void setAttributes(List<String> attributes) {
		this.attributes = attributes;
	}

	public void setExtras(Map<String, ObjectNode> nodes) {
		this.extras = nodes;
	}

	public Map<String, ObjectNode> getExtras() {
		return extras;
	}

	public double getWeight() {
		return weight;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}

	public Map<Integer, LanguageSpecificInfo> getInfos() {
		return infos;
	}

	public void setInfos(Map<Integer, LanguageSpecificInfo> infos) {
		this.infos = infos;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getSpu() {
		return spu;
	}

	public void setSpu(String spu) {
		this.spu = spu;
	}

	protected static String toLowerCase(String s) {
		return s != null ? s.toLowerCase() : null;
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public List<String> getRelatedSku() {
		return relatedSku;
	}

	public void setRelatedSku(List<String> relatedSku) {
		this.relatedSku = relatedSku;
	}

	public List<Integer> getStoragid() {
		return storagid;
	}

	public void setStoragid(List<Integer> storagid) {
		this.storagid = storagid;
	}

	public List<Date> getTagDate() {
		return tagDate;
	}

	public void setTagDate(List<Date> tagDate) {
		this.tagDate = tagDate;
	}

	public Map<String, ProductTypeInfo> getTagsType() {
		return tagsType;
	}

	public void setTagsType(Map<String, ProductTypeInfo> tagsType) {
		this.tagsType = tagsType;
	}

	public List<RecommendDoc> getCategoryRecommend() {
		return categoryRecommend;
	}

	public void setCategoryRecommend(List<RecommendDoc> categoryRecommend) {
		this.categoryRecommend = categoryRecommend;
	}

}
