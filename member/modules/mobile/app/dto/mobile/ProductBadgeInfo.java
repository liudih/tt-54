package dto.mobile;

import java.io.Serializable;

import valueobjects.price.Price;

public class ProductBadgeInfo implements Serializable {

	private static final long serialVersionUID = -2350797395796742449L;

	private String title;

	private String title_default;

	private String url;

	private String imageUrl;

	private String listingId;

	private Price price;

	private String collectDate;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		if (title == null) {
			title = "";
		}
		this.title = title;
	}

	public String getTitle_default() {
		return title_default;
	}

	public void setTitle_default(String title_default) {
		if (title_default == null) {
			title_default = "";
		}
		this.title_default = title_default;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		if (url == null) {
			url = "";
		}
		this.url = url;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		if (imageUrl == null) {
			imageUrl = "";
		}
		this.imageUrl = imageUrl;
	}

	public String getListingId() {
		return listingId;
	}

	public void setListingId(String listingId) {
		if (listingId == null) {
			listingId = "";
		}
		this.listingId = listingId;
	}

	public Price getPrice() {
		return price;
	}

	public void setPrice(Price price) {
		this.price = price;
	}

	public String getCollectDate() {
		return collectDate;
	}

	public void setCollectDate(String collectDate) {
		if (collectDate == null) {
			collectDate = "";
		}
		this.collectDate = collectDate;
	}

}
