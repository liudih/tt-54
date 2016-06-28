package dto.mobile;

import utils.ValidataUtils;

public class OdGoods {

	private String gid;// listing编号
	private String title;// 名称
	private String imgurl;// 图片链接
	private Integer qty;// 数量
	private Double sale;// 单价
	private Double pcs;// 原价格
	private String sku;// sku
	private String atts;// 商品对应的属性
	private Double score;// 商品评分
	private Integer count;// 商品评论总数
	private boolean isreview;// 是否评论过

	public String getGid() {
		return ValidataUtils.validataStr(gid);
	}

	public void setGid(String gid) {
		this.gid = gid;
	}

	public String getTitle() {
		return ValidataUtils.validataStr(title);
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getImgurl() {
		return ValidataUtils.validataStr(imgurl);
	}

	public void setImgurl(String imgurl) {
		this.imgurl = imgurl;
	}

	/*
	 * public String getUrl() { return ValidataUtils.validataStr(url); }
	 * 
	 * public void setUrl(String url) { this.url = url; }
	 */

	public Integer getQty() {
		return ValidataUtils.validataInt(qty);
	}

	public void setQty(Integer qty) {
		this.qty = qty;
	}

	public Double getSale() {
		return ValidataUtils.validataDouble(sale);
	}

	public void setSale(Double sale) {
		this.sale = sale;
	}

	/*
	 * public Double getTotalPrice() { return
	 * ValidataUtils.validataDouble(totalPrice); }
	 * 
	 * public void setTotalPrice(Double totalPrice) { this.totalPrice =
	 * totalPrice; }
	 */

	public Double getPcs() {
		return ValidataUtils.validataDouble(pcs);
	}

	public void setPcs(Double pcs) {
		this.pcs = pcs;
	}

	public String getSku() {
		return ValidataUtils.validataStr(sku);
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	public String getAtts() {
		return ValidataUtils.validataStr(atts);
	}

	public void setAtts(String atts) {
		this.atts = atts;
	}

	public Double getScore() {
		return ValidataUtils.validataDouble(score);
	}

	public void setScore(Double score) {
		this.score = score;
	}

	public Integer getCount() {
		return ValidataUtils.validataInt(count);
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public boolean isIsreview() {
		return isreview;
	}

	public void setIsreview(boolean isreview) {
		this.isreview = isreview;
	}
}
