package valueobjects.product;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import play.twirl.api.Html;
import valueobjects.price.Price;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class ProductBadge implements Serializable {

	private static final long serialVersionUID = 2437674587250818459L;

	String title;
	String title_default;
	String url; // 路由地址
	String imageUrl; // 图片地址
	String listingId;
	Price price;
	transient List<Html> extended = Lists.newLinkedList(); // for reviews / rating from
													// other modules
	transient Map<String, Html> htmlmap = Maps.newHashMap();
	
	transient List<dto.product.ProductSellingPoints> sellingPoints = Lists
			.newLinkedList(); // 卖点
	String collectDate; // 收藏日期

	public String getTitle() {
		return title == null ? title_default.trim() : title.trim();
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTitle_default() {
		return title_default;
	}

	public void setTitle_default(String title_default) {
		this.title_default = title_default;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
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

	public Price getPrice() {
		return price;
	}

	public void setPrice(Price price) {
		this.price = price;
	}

	public List<Html> getExtended() {
		return extended;
	}

	public void setExtended(List<Html> extended) {
		this.extended = extended;
	}

	public List<dto.product.ProductSellingPoints> getSellingPoints() {
		return sellingPoints;
	}

	public void setSellingPoints(
			List<dto.product.ProductSellingPoints> sellingPoints) {
		this.sellingPoints = sellingPoints;
	}

	public String getCollectDate() {
		return collectDate;
	}

	public void setCollectDate(String collectDate) {
		this.collectDate = collectDate;
	}

	public Map<String, Html> getHtmlmap() {
		return htmlmap;
	}

	public void setHtmlmap(Map<String, Html> htmlmap) {
		this.htmlmap = htmlmap;
	}

}
