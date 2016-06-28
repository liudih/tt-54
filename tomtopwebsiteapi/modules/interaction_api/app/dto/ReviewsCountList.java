package dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import services.base.utils.DateFormatUtils;

public class ReviewsCountList implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer commentId;

	private String ccomment;

	private Integer iprice;

	private Integer iquality;

	private Integer ishipping;

	private Integer iusefulness;

	private Double foverallrating;

	private Date dcreatedate;

	private Integer istate;

	private String bName;

	private List<String> commentPhotosUrl;

	private String commentVideoUrl;

	private Integer helpfulqty;

	private Integer nothelpfulqty;

	private String helpfulcode;

	private String email;

	private String title;

	public ReviewsCountList(Integer commentId, String ccomment, Integer iprice,
			Integer iquality, Integer ishipping, Integer iusefulness,
			Double foverallrating, Date dcreatedate, Integer istate,
			String bName, List<String> commentPhotosUrl,
			String commentVideoUrl, Integer helpfulqty, Integer nothelpfulqty,
			String helpfulcode, String email, String title) {
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
		this.commentPhotosUrl = commentPhotosUrl;
		this.commentVideoUrl = commentVideoUrl;
		this.helpfulqty = helpfulqty;
		this.nothelpfulqty = nothelpfulqty;
		this.helpfulcode = helpfulcode;
		this.email = email;
		this.title = title;
	}

	public Integer getCommentId() {
		return commentId;
	}

	public String getCcomment() {
		return ccomment;
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

	public Integer getIstate() {
		return istate;
	}

	public String getbName() {
		return bName;
	}

	public String getDcreatedate() {
		return DateFormatUtils.getDateTimeMDYHMS(dcreatedate);
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

	public String getEmail() {
		return email;
	}

	public String getTitle() {
		return title;
	}

}