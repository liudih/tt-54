package valueobjects.interaction;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import services.base.utils.DateFormatUtils;

public class ReviewsInMemberCenter implements Serializable  {
	private static final long serialVersionUID = 1L;

	private Integer commentid;

	private String ccomment;

	private Integer iprice;

	private Integer iquality;

	private Integer ishipping;

	private Integer iusefulness;

	private Double foverallrating;

	private Date dcreatedate;

	private Integer istate;

	private String productSmallImageUrl;

	private List<String> commentPhotosUrl;

	private String commentVideoUrl;

	private String productUrl;

	public ReviewsInMemberCenter(Integer commentid, String ccomment,
			Integer iprice, Integer iquality, Integer ishipping,
			Integer iusefulness, Double foverallrating, Date dcreatedate,
			Integer istate, String productSmallImageUrl,
			List<String> commentPhotosUrl, String commentVideoUrl,
			String productUrl) {
		super();
		this.commentid = commentid;
		this.ccomment = ccomment;
		this.iprice = iprice;
		this.iquality = iquality;
		this.ishipping = ishipping;
		this.iusefulness = iusefulness;
		this.foverallrating = foverallrating;
		this.dcreatedate = dcreatedate;
		this.istate = istate;
		this.productSmallImageUrl = productSmallImageUrl;
		this.commentPhotosUrl = commentPhotosUrl;
		this.commentVideoUrl = commentVideoUrl;
		this.productUrl = productUrl;
	}

	public Integer getCommentid() {
		return commentid;
	}

	public String getCcomment() {
		return ccomment;
	}

	public List<String> getCommentPhotosUrl() {
		return commentPhotosUrl;
	}

	public String getCommentVideoUrl() {
		return commentVideoUrl;
	}

	public String getDcreatedate() {
		return DateFormatUtils.getDateTimeDDMMYYYY(dcreatedate);
	}

	public String getProductSmallImageUrl() {
		return productSmallImageUrl;
	}

	public Integer getIpriceStarWidth() {
		if (null != iprice) {
			return (int) (((iprice * 0.1) / 5) * 1000);
		}
		return 0;
	}

	public int getFoverallratingStarWidth() {
		if (null != foverallrating) {
			return (int) (((foverallrating * 0.1) / 5) * 1000);
		}
		return 0;
	}

	public Integer getIqualityStarWidth() {
		if (null != iquality) {
			return (int) (((iquality * 0.1) / 5) * 1000);
		}
		return 0;
	}

	public Integer getIshippingStarWidth() {
		if (null != ishipping) {
			return (int) (((ishipping * 0.1) / 5) * 1000);
		}
		return 0;
	}

	public Integer getIusefulness() {
		if (null != iusefulness) {
			return (int) (((iusefulness * 0.1) / 5) * 1000);
		}
		return 0;
	}

	public String getIstate() {
		if (null != istate) {
			if (istate == 0) {
				return "Pending";
			} else if (istate == 1) {
				return "Approved";
			} else if (istate == 2) {
				return "Failed";
			}
		}
		return "Pending";
	}
	public String getProductUrl() {
		return productUrl;
	}
}