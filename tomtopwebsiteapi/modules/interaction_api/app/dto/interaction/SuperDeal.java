package dto.interaction;

import java.io.Serializable;
import java.util.Date;

import services.base.utils.DateFormatUtils;

public class SuperDeal implements Serializable {
	private static final long serialVersionUID = 1L;
	private Integer iid;

	private String clistingid;

	private Integer icategoryrootid;

	private String csku;

	private String ccreateuser;

	private Date dcreatedate;

	private Boolean bshow;

	private Integer iwebsiteid;

	public Integer getIid() {
		return iid;
	}

	public void setIid(Integer iid) {
		this.iid = iid;
	}

	public String getClistingid() {
		return clistingid;
	}

	public void setClistingid(String clistingid) {
		this.clistingid = clistingid;
	}

	public Integer getIcategoryrootid() {
		return icategoryrootid;
	}

	public void setIcategoryrootid(Integer icategoryrootid) {
		this.icategoryrootid = icategoryrootid;
	}

	public String getCsku() {
		return csku;
	}

	public void setCsku(String csku) {
		this.csku = csku;
	}

	public String getCcreateuser() {
		return ccreateuser;
	}

	public void setCcreateuser(String ccreateuser) {
		this.ccreateuser = ccreateuser;
	}

	public Date getDcreatedate() {
		return dcreatedate;
	}

	public void setDcreatedate(Date dcreatedate) {
		this.dcreatedate = dcreatedate;
	}

	public String getDate() {
		return DateFormatUtils.getStrFromYYYYMMDDHHMMSS(dcreatedate);
	}

	public Boolean getBshow() {
		return bshow;
	}

	public void setBshow(Boolean bshow) {
		this.bshow = bshow;
	}

	public Integer getIwebsiteid() {
		return iwebsiteid;
	}

	public void setIwebsiteid(Integer iwebsiteid) {
		this.iwebsiteid = iwebsiteid;
	}

}