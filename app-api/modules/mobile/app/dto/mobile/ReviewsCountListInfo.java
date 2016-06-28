package dto.mobile;

import java.util.ArrayList;
import java.util.List;

public class ReviewsCountListInfo {

	/**
	 * 评论ID
	 */
	private Integer commentId;

	/**
	 * 评论
	 */
	private String ccomment;

	/**
	 * 价格评级
	 */
	private Integer iprice;

	/**
	 * 品质评级
	 */
	private Integer iquality;

	/**
	 * 物流评级
	 */
	private Integer ishipping;

	/**
	 * 有用评级
	 */
	private Integer iusefulness;

	/**
	 * 综合评级
	 */
	private Double foverallrating;

	/**
	 * 创建时间
	 */
	private Long dcreatedate;

	/**
	 * 状态(0未审、1通过、2未通过)
	 */
	private Integer istate;

	/**
	 * 名称
	 */
	private String bName;

	/**
	 * 用户头像
	 */
	private String memberPhoto;

	/**
	 * 评论图片地址
	 */
	private List<String> commentPhotosUrl;

	/**
	 * 评论视频地址
	 */
	private String commentVideoUrl;

	/**
	 * 有用数量
	 */
	private Integer helpfulqty;

	/**
	 * 没用数量
	 */
	private Integer nothelpfulqty;

	/**
	 * 是否有帮助(0:没有帮助1:有帮助
	 */
	private String helpfulcode;

	public ReviewsCountListInfo(Integer commentId, String ccomment,
			Integer iprice, Integer iquality, Integer ishipping,
			Integer iusefulness, Double foverallrating, Long dcreatedate,
			Integer istate, String bName, String memberPhoto,
			List<String> commentPhotosUrl, String commentVideoUrl,
			Integer helpfulqty, Integer nothelpfulqty, String helpfulcode) {
		super();
		this.commentId = commentId;
		this.ccomment = ccomment;
		this.iprice = iprice;
		this.iquality = iquality;
		this.ishipping = ishipping;
		this.iusefulness = iusefulness;
		this.foverallrating = foverallrating;
		this.dcreatedate = dcreatedate;
		this.istate = istate;
		this.bName = bName;
		this.memberPhoto = memberPhoto;
		this.commentPhotosUrl = commentPhotosUrl;
		this.commentVideoUrl = commentVideoUrl;
		this.helpfulqty = helpfulqty;
		this.nothelpfulqty = nothelpfulqty;
		this.helpfulcode = helpfulcode;
	}

	public Integer getCommentId() {
		return commentId;
	}

	public Integer getIprice() {
		return iprice;
	}

	public void setIprice(Integer iprice) {
		if (null == iprice) {
			iprice = 0;
		}
		this.iprice = iprice;
	}

	public Integer getIquality() {
		return iquality;
	}

	public void setIquality(Integer iquality) {
		if (null == iquality) {
			iquality = 0;
		}
		this.iquality = iquality;
	}

	public Integer getIshipping() {
		return ishipping;
	}

	public void setIshipping(Integer ishipping) {
		if (null == ishipping) {
			ishipping = 0;
		}
		this.ishipping = ishipping;
	}

	public Double getFoverallrating() {
		return foverallrating;
	}

	public void setFoverallrating(Double foverallrating) {
		if (null == foverallrating) {
			foverallrating = 0d;
		}
		this.foverallrating = foverallrating;
	}

	public String getHelpfulcode() {
		return helpfulcode;
	}

	public void setHelpfulcode(String helpfulcode) {
		if (null == helpfulcode) {
			helpfulcode = "";
		}
		this.helpfulcode = helpfulcode;
	}

	public void setCommentId(Integer commentId) {
		if (null == commentId) {
			commentId = 0;
		}
		this.commentId = commentId;
	}

	public void setCcomment(String ccomment) {
		if (null == ccomment) {
			ccomment = "";
		}
		this.ccomment = ccomment;
	}

	public void setIusefulness(Integer iusefulness) {
		if (null == iusefulness) {
			iusefulness = 0;
		}
		this.iusefulness = iusefulness;
	}

	public void setDcreatedate(Long dcreatedate) {
		if (null == dcreatedate) {
			dcreatedate = 0L;
		}
		this.dcreatedate = dcreatedate;
	}

	public void setIstate(Integer istate) {
		if (null == istate) {
			istate = 0;
		}
		this.istate = istate;
	}

	public void setbName(String bName) {
		this.bName = bName;
	}

	public void setCommentPhotosUrl(List<String> commentPhotosUrl) {
		if (null == commentPhotosUrl) {
			commentPhotosUrl = new ArrayList<String>();
		}
		this.commentPhotosUrl = commentPhotosUrl;
	}

	public void setCommentVideoUrl(String commentVideoUrl) {
		if (null == commentVideoUrl) {
			commentVideoUrl = "";
		}
		this.commentVideoUrl = commentVideoUrl;
	}

	public void setHelpfulqty(Integer helpfulqty) {
		if (null == helpfulqty) {
			helpfulqty = 0;
		}
		this.helpfulqty = helpfulqty;
	}

	public void setNothelpfulqty(Integer nothelpfulqty) {
		if (null == nothelpfulqty) {
			nothelpfulqty = 0;
		}
		this.nothelpfulqty = nothelpfulqty;
	}

	public String getCcomment() {
		return ccomment;
	}

	public Integer getIpriceStarWidth() {
		if (null != iprice) {
			return iprice;
		}
		return 0;
	}

	public int getFoverallratingStarWidth() {
		if (null != foverallrating) {
			return foverallrating.intValue();
		}
		return 0;
	}

	public Integer getIqualityStarWidth() {
		if (null != iquality) {
			return iquality;
		}
		return 0;
	}

	public Integer getIshippingStarWidth() {
		if (null != ishipping) {
			return iquality;
		}
		return 0;
	}

	public Integer getIusefulness() {
		if (null != iusefulness) {
			return iusefulness;
		}
		return 0;
	}

	public Integer getIstate() {
		return istate;
	}

	public String getbName() {
		return bName;
	}

	public Long getDcreatedate() {
		return dcreatedate;
	}

	public List<String> getCommentPhotosUrl() {
		return commentPhotosUrl;
	}

	public String getCommentVideoUrl() {
		return commentVideoUrl;
	}

	public Integer getHelpfulqty() {
		return helpfulqty;
	}

	public Integer getNothelpfulqty() {
		return nothelpfulqty;
	}

	public String getMemberPhoto() {
		return memberPhoto;
	}

	public void setMemberPhoto(String memberPhoto) {
		this.memberPhoto = memberPhoto;
	}
}