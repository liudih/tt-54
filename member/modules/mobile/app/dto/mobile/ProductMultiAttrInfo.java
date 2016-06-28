package dto.mobile;

/**
 * 商品多属性
 * 
 * @author Administrator
 *
 */
public class ProductMultiAttrInfo {
	/**
	 * 商品Id
	 */
	private String lid;

	/**
	 * SKU
	 */
	private String sku;

	/**
	 * 属性KEY
	 */
	private String key;

	/**
	 * 属性value
	 */
	private String value;

	/**
	 * 是否显示
	 */
	private boolean show;

	/**
	 * 是否显示图片
	 */
	private boolean showimg;

	/**
	 * 标题
	 */
	private String title;

	/**
	 * URL
	 */
	private String url;

	/**
	 * 价格
	 */
	private Double price;

	/**
	 * 图片地址
	 */
	private String imgUrl;

	/**
	 * 货币
	 */
	private String currency;

	/**
	 * 符号
	 */
	private String symbol;

	public String getLid() {
		return lid;
	}

	public void setLid(String lid) {
		this.lid = lid;
	}

	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public boolean isShow() {
		return show;
	}

	public void setShow(boolean show) {
		this.show = show;
	}

	public boolean isShowimg() {
		return showimg;
	}

	public void setShowimg(boolean showimg) {
		this.showimg = showimg;
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
}
