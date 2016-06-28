package dto.product;

import java.io.Serializable;
import java.util.Date;

public class ProductMultiattributeEntity implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String clistingid;

	private String csku;

	private String ckey;

	private String cvalue;

	private boolean bshow;

	private boolean bshowimg;
	
	private String title;
	
	private String url;
	
	private Double price;
	
	private String imgUrl;
	
	private String currency;
	
	private String symbol;
	
	private Double basePrice;
	
	private Boolean isDiscounted;
	
	private Double discount;
	
	private Date validTo;
	
	private Long validToBySeconds;
	
	public ProductMultiattributeEntity(String clistingid, String csku,
			String ckey, String cvalue, boolean bshow, boolean bshowimg,
			String title, String url, Double price, String imgUrl,
			String currency, String symbol) {
		super();
		this.clistingid = clistingid;
		this.csku = csku;
		this.ckey = ckey;
		this.cvalue = cvalue;
		this.bshow = bshow;
		this.bshowimg = bshowimg;
		this.title = title;
		this.url = url;
		this.price = price;
		this.imgUrl = imgUrl;
		this.currency = currency;
		this.symbol = symbol;
	}

	public String getClistingid() {
		return clistingid;
	}

	public void setClistingid(String clistingid) {
		this.clistingid = clistingid;
	}

	public String getCsku() {
		return csku;
	}

	public void setCsku(String csku) {
		this.csku = csku;
	}

	public String getCkey() {
		return ckey;
	}

	public void setCkey(String ckey) {
		this.ckey = ckey;
	}

	public String getCvalue() {
		return cvalue;
	}

	public void setCvalue(String cvalue) {
		this.cvalue = cvalue;
	}

	public boolean isBshow() {
		return bshow;
	}

	public void setBshow(boolean bshow) {
		this.bshow = bshow;
	}

	public boolean isBshowimg() {
		return bshowimg;
	}

	public void setBshowimg(boolean bshowimg) {
		this.bshowimg = bshowimg;
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

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public Double getBasePrice() {
		return basePrice;
	}

	public void setBasePrice(Double basePrice) {
		this.basePrice = basePrice;
	}

	public Boolean getIsDiscounted() {
		return isDiscounted;
	}

	public void setIsDiscounted(Boolean isDiscounted) {
		this.isDiscounted = isDiscounted;
	}

	public Double getDiscount() {
		return discount;
	}

	public void setDiscount(Double discount) {
		this.discount = discount;
	}

	public Date getValidTo() {
		return validTo;
	}

	public void setValidTo(Date validTo) {
		this.validTo = validTo;
	}

	public Long getValidToBySeconds() {
		if(this.validTo!=null){
			return (this.validTo.getTime() - System.currentTimeMillis());
		}else{
			return 0l;
		}
	}
}