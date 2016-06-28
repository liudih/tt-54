package valueobjects.product;

import java.io.Serializable;

public class ProductBadgeConvert implements Serializable {
	
	private static final long serialVersionUID = 2437674587250818459L;
	private String title;
	private String url;
	private String imageUrl;
	private Double discount;
	private Double nowprice;
	private Double origprice;
	private String currencySymbol;
	
	public String getCurrencySymbol() {
		return currencySymbol;
	}

	public void setCurrencySymbol(String currencySymbol) {
		this.currencySymbol = currencySymbol;
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

	public Double getDiscount() {
		return discount;
	}

	public void setDiscount(Double discount) {
		this.discount = discount;
	}

	public Double getNowprice() {
		return nowprice;
	}

	public void setNowprice(Double nowprice) {
		this.nowprice = nowprice;
	}

	public Double getOrigprice() {
		return origprice;
	}

	public void setOrigprice(Double origprice) {
		this.origprice = origprice;
	}

}
