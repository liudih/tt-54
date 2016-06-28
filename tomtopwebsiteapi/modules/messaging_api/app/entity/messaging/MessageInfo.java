package entity.messaging;

import java.io.Serializable;
import java.util.Date;

public class MessageInfo implements Serializable {

	private static final long serialVersionUID = 1L;

	private int iid;
	private int isendid;
	private String csubject;
	private String ccontent;
	private String cemail;
	private String csendname;
	private Date dcreatedate;

	public int getIid() {
		return iid;
	}

	public void setIid(int iid) {
		this.iid = iid;
	}

	public int getIsendid() {
		return isendid;
	}

	public void setIsendid(int isendid) {
		this.isendid = isendid;
	}

	public String getCsubject() {
		return csubject;
	}

	public void setCsubject(String csubject) {
		this.csubject = csubject;
	}

	public String getCcontent() {
		return ccontent;
	}

	public void setCcontent(String ccontent) {
		this.ccontent = ccontent;
	}

	public String getCemail() {
		return cemail;
	}

	public void setCemail(String cemail) {
		this.cemail = cemail;
	}

	public String getCsendname() {
		return csendname;
	}

	public void setCsendname(String csendname) {
		this.csendname = csendname;
	}

	public Date getDcreatedate() {
		return dcreatedate;
	}

	public void setDcreatedate(Date dcreatedate) {
		this.dcreatedate = dcreatedate;
	}

}
