package dto.interaction;

import java.io.Serializable;
import java.util.Date;

public class InteractionCommentHelpQty implements Serializable  {
	private static final long serialVersionUID = 1L;

	private Integer iid;
	private Integer helpfulqty;
	private Integer nothelpfulqty;
	private Integer commentid;
	private Date dcreatedate;

	public Integer getIid() {
		return iid;
	}

	public void setIid(Integer iid) {
		this.iid = iid;
	}

	public Integer getHelpfulqty() {
		return helpfulqty;
	}

	public void setHelpfulqty(Integer helpfulqty) {
		this.helpfulqty = helpfulqty;
	}

	public Integer getNothelpfulqty() {
		return nothelpfulqty;
	}

	public void setNothelpfulqty(Integer nothelpfulqty) {
		this.nothelpfulqty = nothelpfulqty;
	}

	public Integer getCommentid() {
		return commentid;
	}

	public void setIcommentid(Integer commentid) {
		this.commentid = commentid;
	}

	public Date getDcreatedate() {
		return dcreatedate;
	}

	public void setDcreatedate(Date dcreatedate) {
		this.dcreatedate = dcreatedate;
	}

	public void setCommentid(Integer commentid) {
		this.commentid = commentid;
	}

}
