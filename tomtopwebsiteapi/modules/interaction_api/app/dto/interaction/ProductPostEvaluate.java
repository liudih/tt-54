package dto.interaction;

import java.io.Serializable;
import java.util.Date;

public class ProductPostEvaluate implements Serializable  {
	private static final long serialVersionUID = 1L;

	private Integer ipostid;

	private Integer ihelpfulcode;

	private String cmemberemail;

	private Date dcreatedate;

	public Integer getIhelpfulcode() {
		return ihelpfulcode;
	}

	public Integer getIpostid() {
		return ipostid;
	}

	public void setIpostid(Integer ipostid) {
		this.ipostid = ipostid;
	}

	public void setIhelpfulcode(Integer ihelpfulcode) {
		this.ihelpfulcode = ihelpfulcode;
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

}