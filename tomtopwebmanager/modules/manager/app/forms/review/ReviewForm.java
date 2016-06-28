package forms.review;

import util.AppsUtil;

public class ReviewForm {

	private Integer iid;
	private String csku;// sku
	private String cemail;// 邮件
	private String cdtl; // 评论内容
	private String ddate;// 时间
	private Integer usefulness;
	private Integer shipping;
	private Integer price;
	private Integer quality;
	private Integer websiteid;

	public Integer getIid() {
		return iid;
	}

	public void setIid(Integer iid) {
		this.iid = iid;
	}

	public String getCsku() {
		return AppsUtil.trim(csku);
	}

	public void setCsku(String csku) {
		this.csku = csku;
	}

	public String getCemail() {
		return AppsUtil.trim(cemail);
	}

	public void setCemail(String cemail) {
		this.cemail = cemail;
	}

	public String getCdtl() {
		return AppsUtil.trim(cdtl);
	}

	public void setCdtl(String cdtl) {
		this.cdtl = cdtl;
	}

	public String getDdate() {
		return AppsUtil.trim(ddate);
	}

	public void setDdate(String ddate) {
		this.ddate = ddate;
	}

	public Integer getUsefulness() {
		return AppsUtil.checkInt(usefulness);
	}

	public void setUsefulness(Integer usefulness) {
		this.usefulness = usefulness;
	}

	public Integer getShipping() {
		return AppsUtil.checkInt(shipping);
	}

	public void setShipping(Integer shipping) {
		this.shipping = shipping;
	}

	public Integer getPrice() {
		return AppsUtil.checkInt(price);
	}

	public void setPrice(Integer price) {
		this.price = price;
	}

	public Integer getQuality() {
		return AppsUtil.checkInt(quality);
	}

	public void setQuality(Integer quality) {
		this.quality = quality;
	}

	public double getFoverallrating() {
		double foverallrating = (double) usefulness + (double) shipping
				+ (double) price + (double) quality;
		foverallrating = foverallrating / 4;

		return foverallrating;
	}

	public Integer getWebsiteid() {
		return websiteid;
	}

	public void setWebsiteid(Integer websiteid) {
		this.websiteid = websiteid;
	}
}
