package dto.mobile;

public class CartItemInfo {
	private String iid;

	// private String cparentId;

	private String gid;

	private String title;

	private String imgurl;

	private Integer qty;

	private Integer stock;

	private Double sale;

	private Double pcs;

	private int wish;

	private String sku;
	// 附加信息
	private String extra;

	private String attrs;

	public String getIid() {
		return iid;
	}

	public void setIid(String iid) {
		this.iid = iid;
	}

	// public String getCparentId() {
	// return cparentId;
	// }
	//
	// public void setCparentId(String cparentId) {
	// this.cparentId = cparentId;
	// }

	public String getGid() {
		return gid;
	}

	public void setGid(String gid) {
		this.gid = gid;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getImgurl() {
		return imgurl;
	}

	public void setImgurl(String imgurl) {
		this.imgurl = imgurl;
	}

	public Integer getQty() {
		return qty;
	}

	public void setQty(Integer qty) {
		this.qty = qty;
	}

	public Integer getStock() {
		return stock;
	}

	public void setStock(Integer stock) {
		this.stock = stock;
	}

	public Double getSale() {
		return sale;
	}

	public void setSale(Double sale) {
		this.sale = sale;
	}

	public int getWish() {
		return wish;
	}

	public void setWish(int wish) {
		this.wish = wish;
	}

	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	public String getExtra() {
		return extra;
	}

	public void setExtra(String extra) {
		this.extra = extra;
	}

	public String getAttrs() {
		return attrs;
	}

	public void setAttrs(String attrs) {
		this.attrs = attrs;
	}

	public Double getPcs() {
		return pcs;
	}

	public void setPcs(Double pcs) {
		this.pcs = pcs;
	}

}
