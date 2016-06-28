package forms.interaction.review;

import java.io.Serializable;
import java.sql.Date;

public class ProductReviewForm implements Serializable {

	private static final long serialVersionUID = 1L;

	private String listingID;

	private String scku;

	private String nickname;// 昵称

	private String email;

	private String ccomment;

	private Integer iscore;

	private Integer iprice;// 价格评级

	private Integer iquality;// 质量

	private Integer ishipping;// 外观

	private Integer iusefulness;

	private Double foverallrating;

	private String reviews;

	private Date dcreatedate;

	private Integer index;

	private String indexs;

	private String orderDetailId; // 订单ID

	private String isVip; // 是否为达人

	@Override
	public String toString() {
		return "ProductReviewForm [listingID=" + listingID + ", scku=" + scku
				+ ", nickname=" + nickname + ", email=" + email + ", ccomment="
				+ ccomment + ", iscore=" + iscore + ", iprice=" + iprice
				+ ", iquality=" + iquality + ", ishipping=" + ishipping
				+ ", iusefulness=" + iusefulness + ", foverallrating="
				+ foverallrating + ", reviews=" + reviews + ", dcreatedate="
				+ dcreatedate + ", cimageurl=" + ", index=" + index
				+ ", indexs=" + indexs + ", videoUrl=" + videoUrl
				+ ", videoTitle=" + videoTitle + "]";
	}

	public Integer getIndex() {
		return index;
	}

	public void setIndex(Integer index) {
		this.index = index;
	}

	public String getIndexs() {
		return indexs;
	}

	public void setIndexs(String indexs) {
		this.indexs = indexs;
	}

	private String videoUrl; // video路径

	private String videoTitle;// video标题

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getListingID() {
		return listingID;
	}

	public void setListingID(String listingID) {
		this.listingID = listingID;
	}

	public String getScku() {
		return scku;
	}

	public void setScku(String scku) {
		this.scku = scku;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getCcomment() {
		return ccomment;
	}

	public void setCcomment(String ccomment) {
		this.ccomment = ccomment;
	}

	public Integer getIscore() {
		return iscore;
	}

	public void setIscore(Integer iscore) {
		this.iscore = iscore;
	}

	public Integer getIprice() {
		return iprice;
	}

	public void setIprice(Integer iprice) {
		this.iprice = iprice;
	}

	public Integer getIquality() {
		return iquality;
	}

	public void setIquality(Integer iquality) {
		this.iquality = iquality;
	}

	public Integer getIshipping() {
		return ishipping;
	}

	public void setIshipping(Integer ishipping) {
		this.ishipping = ishipping;
	}

	public Integer getIusefulness() {
		return iusefulness;
	}

	public void setIusefulness(Integer iusefulness) {
		this.iusefulness = iusefulness;
	}

	public Double getFoverallrating() {
		return foverallrating;
	}

	public void setFoverallrating(Double foverallrating) {
		this.foverallrating = foverallrating;
	}

	public String getReviews() {
		return reviews;
	}

	public void setReviews(String reviews) {
		this.reviews = reviews;
	}

	public Date getDcreatedate() {
		return dcreatedate;
	}

	public void setDcreatedate(Date dcreatedate) {
		this.dcreatedate = dcreatedate;
	}

	public String getVideoUrl() {
		return videoUrl;
	}

	public void setVideoUrl(String videoUrl) {
		this.videoUrl = videoUrl;
	}

	public String getVideoTitle() {
		return videoTitle;
	}

	public void setVideoTitle(String videoTitle) {
		this.videoTitle = videoTitle;
	}

	public String getOrderDetailId() {
		return orderDetailId;
	}

	public void setOrderDetailId(String orderDetailId) {
		this.orderDetailId = orderDetailId;
	}

	public String getIsVip() {
		return isVip;
	}

	public void setIsVip(String isVip) {
		this.isVip = isVip;
	}

}
