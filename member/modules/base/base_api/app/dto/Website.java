package dto;

import java.io.Serializable;
import java.util.Date;

public class Website implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer iid;

	private Integer iplatformid;

	private String ccode;

	private String curl;

	private Integer ilanguageid;

	private Integer icurrencyid;

	private String ccreateuser;

	private Date dcreatedate;

	private boolean bfallback;

	private Integer idefaultshippingcountry;

	private Integer idefaultshippingstorage;
	
	
	public Website() {
		
	}
	
	public Website(Integer iid, String ccode) {
		this.iid = iid;
		this.ccode = ccode;
	}

	public Integer getIid() {
		return iid;
	}

	public void setIid(Integer iid) {
		this.iid = iid;
	}

	public Integer getIplatformid() {
		return iplatformid;
	}

	public void setIplatformid(Integer iplatformid) {
		this.iplatformid = iplatformid;
	}

	public String getCcode() {
		return ccode;
	}

	public void setCcode(String ccode) {
		this.ccode = ccode == null ? null : ccode.trim();
	}

	public String getCurl() {
		return curl;
	}

	public void setCurl(String curl) {
		this.curl = curl;
	}

	public Integer getIlanguageid() {
		return ilanguageid;
	}

	public void setIlanguageid(Integer ilanguageid) {
		this.ilanguageid = ilanguageid;
	}

	public Integer getIcurrencyid() {
		return icurrencyid;
	}

	public void setIcurrencyid(Integer icurrencyid) {
		this.icurrencyid = icurrencyid;
	}

	public boolean isBfallback() {
		return bfallback;
	}

	public void setBfallback(boolean bfallback) {
		this.bfallback = bfallback;
	}

	public String getCcreateuser() {
		return ccreateuser;
	}

	public void setCcreateuser(String ccreateuser) {
		this.ccreateuser = ccreateuser == null ? null : ccreateuser.trim();
	}

	public Date getDcreatedate() {
		return dcreatedate;
	}

	public void setDcreatedate(Date dcreatedate) {
		this.dcreatedate = dcreatedate;
	}

	public Integer getIdefaultshippingcountry() {
		return this.idefaultshippingcountry == null ? 0
				: idefaultshippingcountry;
	}

	public void setIdefaultshippingcountry(Integer idefaultshippingcountry) {
		this.idefaultshippingcountry = idefaultshippingcountry;
	}

	public Integer getIdefaultshippingstorage() {
		return idefaultshippingstorage;
	}

	public void setIdefaultshippingstorage(Integer idefaultshippingstorage) {
		this.idefaultshippingstorage = idefaultshippingstorage;
	}

	@Override
	public String toString() {
		return "Website [iid=" + iid + ", iplatformid=" + iplatformid
				+ ", ccode=" + ccode + ", curl=" + curl + ", ilanguageid="
				+ ilanguageid + ", icurrencyid=" + icurrencyid
				+ ", ccreateuser=" + ccreateuser + ", dcreatedate="
				+ dcreatedate + ", bfallback=" + bfallback
				+ ", idefaultshippingcountry=" + idefaultshippingcountry
				+ ", idefaultshippingstorage=" + idefaultshippingstorage + "]";
	}

}