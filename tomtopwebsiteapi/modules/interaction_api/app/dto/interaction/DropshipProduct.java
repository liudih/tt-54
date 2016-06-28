package dto.interaction;

import java.io.Serializable;
import java.util.Date;

public class DropshipProduct implements Serializable  {
	private static final long serialVersionUID = 1L;
	
	private Integer iid;

	private String cemail;

	private String csku;

	private Boolean bstate;

	private Date dcreatedate;

	private Date dupdatestatedate;

	private Integer iwebsiteid;

	public Integer getIid() {
		return iid;
	}

	public void setIid(Integer iid) {
		this.iid = iid;
	}

	public String getCemail() {
		return cemail;
	}

	public void setCemail(String cemail) {
		this.cemail = cemail;
	}

	public String getCsku() {
		return csku;
	}

	public void setCsku(String csku) {
		this.csku = csku;
	}

	public Boolean getBstate() {
		return bstate;
	}

	public void setBstate(Boolean bstate) {
		this.bstate = bstate;
	}

	public Date getDcreatedate() {
		return dcreatedate;
	}

	public void setDcreatedate(Date dcreatedate) {
		this.dcreatedate = dcreatedate;
	}

	public Date getDupdatestatedate() {
		return dupdatestatedate;
	}

	public void setDupdatestatedate(Date dupdatestatedate) {
		this.dupdatestatedate = dupdatestatedate;
	}

	public Integer getIwebsiteid() {
		return iwebsiteid;
	}

	public void setIwebsiteid(Integer iwebsiteid) {
		this.iwebsiteid = iwebsiteid;
	}

	@Override
	public String toString() {
		return "DropshipProduct [iid=" + iid + ", cemail=" + cemail + ", csku="
				+ csku + ", bstate=" + bstate + ", dcreatedate=" + dcreatedate
				+ ", dupdatestatedate=" + dupdatestatedate + "]";
	}

}