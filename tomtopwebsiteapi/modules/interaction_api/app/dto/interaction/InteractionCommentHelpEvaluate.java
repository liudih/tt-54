package dto.interaction;

import java.io.Serializable;
import java.util.Date;

public class InteractionCommentHelpEvaluate  implements Serializable {
	private static final long serialVersionUID = 1L;

	private Integer icommentid;

	private Integer helpfulcode;

	private String cmemberemail;

	private Date dcreatedate;

	public Integer getIcommentid() {
		return icommentid;
	}

	public void setIcommentid(Integer icommentid) {
		this.icommentid = icommentid;
	}

	public Integer getHelpfulcode() {
		return helpfulcode;
	}

	public void setHelpfulcode(Integer helpfulcode) {
		this.helpfulcode = helpfulcode;
	}

	public String getCmemberemail() {
		return cmemberemail;
	}

	public void setCmemberemail(String cmemberemail) {
		this.cmemberemail = cmemberemail;
	}

	public Date getDcreatedate() {
		return dcreatedate;
	}

	public void setDcreatedate(Date dcreatedate) {
		this.dcreatedate = dcreatedate;
	}

	public InteractionCommentHelpEvaluate() {
		super();
/*		this.icommentid = icommentid;
		this.helpfulcode = helpfulcode;
		this.cmemberemail = cmemberemail;*/
	}

}
