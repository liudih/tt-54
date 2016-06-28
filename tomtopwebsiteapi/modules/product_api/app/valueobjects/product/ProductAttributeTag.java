package valueobjects.product;

/**
 * 代表一个小图、一个商品链接、属性键和值，用来展示在属性选择的界面上
 * 
 * @author jiangrb
 *
 */
public class ProductAttributeTag {

	final String listingID;
	final String imageUrl;
	final String key;
	final String value;
	final String productUrl;
	final Boolean showImg;
	final Boolean clickEvent;

	public ProductAttributeTag(String listingID, String key, String value,
			String productUrl, String imageUrl,Boolean showImg, Boolean clickEvent) {
		super();
		this.listingID = listingID;
		this.key = key;
		this.value = value;
		this.productUrl = productUrl;
		this.imageUrl = imageUrl;
		this.showImg = showImg;
		this.clickEvent = clickEvent;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public String getKey() {
		return key;
	}

	public String getValue() {
		return value;
	}

	public String getProductUrl() {
		return productUrl;
	}

	public String getListingID() {
		return listingID;
	}

	public Boolean getShowImg() {
		return showImg;
	}

	public Boolean getClickEvent() {
		return clickEvent;
	}
}
