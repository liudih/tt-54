package com.rabbit.dto.valueobjects.product;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.rabbit.dto.product.ProductSellingPoints;
import com.rabbit.dto.valueobjects.price.Price;
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
	transient List<Object> extended = Lists.newLinkedList(); // for reviews / rating from
													// other modules
	transient Map<String, Object> htmlmap = Maps.newHashMap();
	
	transient List<ProductSellingPoints> sellingPoints = Lists
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

	public List<Object> getExtended() {
		return extended;
	}

	public void setExtended(List<Object> extended) {
		this.extended = extended;
	}

	public List<ProductSellingPoints> getSellingPoints() {
		return sellingPoints;
	}

	public void setSellingPoints(
			List<ProductSellingPoints> sellingPoints) {
		this.sellingPoints = sellingPoints;
	}

	public String getCollectDate() {
		return collectDate;
	}

	public void setCollectDate(String collectDate) {
		this.collectDate = collectDate;
	}

	public Map<String, Object> getHtmlmap() {
		return htmlmap;
	}

	public void setHtmlmap(Map<String, Object> htmlmap) {
		this.htmlmap = htmlmap;
	}

}
