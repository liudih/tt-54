package dto.product;

import java.io.Serializable;
import java.util.Map;

public class ProductMessage implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String title;

	private String listingid;

	private String imageurl;

	private String url;
	
	private String sku;

	private Map<String, ProductMessage> attribute;

	private String parentsku;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getListingid() {
		return listingid;
	}

	public void setListingid(String listingid) {
		this.listingid = listingid;
	}

	public String getImageurl() {
		return imageurl;
	}

	public void setImageurl(String imageurl) {
		this.imageurl = imageurl;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Map<String, ProductMessage> getAttribute() {
		return attribute;
	}

	public void setAttribute(Map<String, ProductMessage> attribute) {
		this.attribute = attribute;
	}

	public String getParentsku() {
		return parentsku;
	}

	public void setParentsku(String parentsku) {
		this.parentsku = parentsku;
	}

	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}
	
}
