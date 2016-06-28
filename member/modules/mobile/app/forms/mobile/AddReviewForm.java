package forms.mobile;

import java.io.Serializable;

import utils.ValidataUtils;

public class AddReviewForm implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String gid; // 商品ID
	private String nickname;// 昵称
	private String comment;// 评论内容
	private Integer price;// 价格评级
	private Integer quality;// 质量
	private Integer usefulness;// 外观
	private Integer shipping;// 邮寄
	private Double overallrating;// 总评
	private String oid; // 订单ID
	private String videourl; // video路径 可为空
	private String videotitle;// video标题 可为空

	public String getGid() {
		return ValidataUtils.validataStr(gid);
	}

	public void setGid(String gid) {
		this.gid = gid;
	}

	public String getNickname() {
		return ValidataUtils.validataStr(nickname);
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getComment() {
		return ValidataUtils.validataStr(comment);
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public Integer getPrice() {
		return ValidataUtils.validataInt(price);
	}

	public void setPrice(Integer price) {
		this.price = price;
	}

	public Integer getQuality() {
		return ValidataUtils.validataInt(quality);
	}

	public void setQuality(Integer quality) {
		this.quality = quality;
	}

	public Integer getUsefulness() {
		return ValidataUtils.validataInt(usefulness);
	}

	public void setUsefulness(Integer usefulness) {
		this.usefulness = usefulness;
	}

	public Integer getShipping() {
		return ValidataUtils.validataInt(shipping);
	}

	public void setShipping(Integer shipping) {
		this.shipping = shipping;
	}

	public Double getOverallrating() {
		return ValidataUtils.validataDouble(overallrating);
	}

	public void setOverallrating(Double overallrating) {
		this.overallrating = overallrating;
	}

	public String getOid() {
		return ValidataUtils.validataStr(oid);
	}

	public void setOid(String oid) {
		this.oid = oid;
	}

	public String getVideourl() {
		return ValidataUtils.validataStr(videourl);
	}

	public void setVideourl(String videourl) {
		this.videourl = videourl;
	}

	public String getVideotitle() {
		return ValidataUtils.validataStr(videotitle);
	}

	public void setVideotitle(String videotitle) {
		this.videotitle = videotitle;
	}

}
