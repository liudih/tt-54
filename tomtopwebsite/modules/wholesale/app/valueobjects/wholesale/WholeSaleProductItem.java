package valueobjects.wholesale;

import java.util.Map;

import dto.product.ProductBase;
import entity.wholesale.WholeSaleProduct;

public class WholeSaleProductItem {
	String title;
	String url;
	String imageUrl;
	ProductBase productBase;
	Map<String, String> attributeMap;
	WholeSaleProduct wholeSaleProduct;
	WholesalePrice wholesalePrice;

	public WholeSaleProductItem() {
	}

	public WholeSaleProductItem(String title, String url, String imageUrl,
			ProductBase productBase, Map<String, String> attributeMap,
			WholeSaleProduct wholeSaleProduct) {
		this.title = title;
		this.url = url;
		this.imageUrl = imageUrl;
		this.productBase = productBase;
		this.attributeMap = attributeMap;
		this.wholeSaleProduct = wholeSaleProduct;
	}

	public WholeSaleProduct getWholeSaleProduct() {
		return wholeSaleProduct;
	}

	public void setWholeSaleProduct(WholeSaleProduct wholeSaleProduct) {
		this.wholeSaleProduct = wholeSaleProduct;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
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

	public ProductBase getProductBase() {
		return productBase;
	}

	public void setProductBase(ProductBase productBase) {
		this.productBase = productBase;
	}

	public Map<String, String> getAttributeMap() {
		return attributeMap;
	}

	public void setAttributeMap(Map<String, String> attributeMap) {
		this.attributeMap = attributeMap;
	}

	public WholesalePrice getWholesalePrice() {
		return wholesalePrice;
	}

	public void setWholesalePrice(WholesalePrice wholesalePrice) {
		this.wholesalePrice = wholesalePrice;
	}
	
}
