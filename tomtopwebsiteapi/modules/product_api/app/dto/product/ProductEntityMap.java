package dto.product;

import java.io.Serializable;
import java.util.Date;

public class ProductEntityMap implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer iid;

	private String clistingid;

	private String csku;

	private String ckeyname;

	private String cvaluename;

	private Integer ikey;

	private Integer ivalue;

	private Boolean bshow;

	private Boolean bshowimg;

	private Integer iwebsiteid;

	private String ccreateuser;

	private Date dcreatedate;

	private Boolean multiattribute;

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

	public String getCsku() {
		return csku;
	}

	public void setCsku(String csku) {
		this.csku = csku;
	}

	public String getCkeyname() {
		return ckeyname;
	}

	public void setCkeyname(String ckeyname) {
		this.ckeyname = ckeyname;
	}

	public String getCvaluename() {
		return cvaluename;
	}

	public void setCvaluename(String cvaluename) {
		this.cvaluename = cvaluename;
	}

	public Boolean isBshow() {
		return bshow;
	}

	public void setBshow(Boolean bshow) {
		this.bshow = bshow;
	}

	public Boolean isBshowimg() {
		return bshowimg;
	}

	public void setBshowimg(Boolean bshowimg) {
		this.bshowimg = bshowimg;
	}

	@Override
	public String toString() {
		return "ProductEntityMap [clistingid=" + clistingid + ", csku=" + csku
				+ ", ckey=" + ckeyname + ", cvalue=" + cvaluename + ", bshow="
				+ bshow + ", bshowimg=" + bshowimg + "]";
	}

	public Integer getIwebsiteid() {
		return iwebsiteid;
	}

	public void setIwebsiteid(Integer iwebsite) {
		this.iwebsiteid = iwebsite;
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

	public Integer getIkey() {
		return ikey;
	}

	public void setIkey(Integer ikey) {
		this.ikey = ikey;
	}

	public Integer getIvalue() {
		return ivalue;
	}

	public void setIvalue(Integer ivalue) {
		this.ivalue = ivalue;
	}

	public Boolean getMultiattribute() {
		return multiattribute;
	}

	public void setMultiattribute(Boolean multiattribute) {
		this.multiattribute = multiattribute;
	}

}