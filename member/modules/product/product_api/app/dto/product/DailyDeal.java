package dto.product;

import java.io.Serializable;
import java.util.Date;

//import services.base.utils.DateFormatUtils;

public class DailyDeal implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer iid;

	private Integer iwebsiteid;

	private String clistingid;

	private String csku;

	private String ccreateuser;

	private Date dcreatedate;

	private boolean bvalid;

	public Integer getIid() {
		return iid;
	}

	public void setIid(Integer iid) {
		this.iid = iid;
	}

	public Integer getIwebsiteid() {
		return iwebsiteid;
	}

	public void setIwebsiteid(Integer iwebsiteid) {
		this.iwebsiteid = iwebsiteid;
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
//		return DateFormatUtils.getStrFromYYYYMMDDHHMMSS(dcreatedate);
		return dcreatedate.toString();
	}

	public boolean isBvalid() {
		return bvalid;
	}

	public void setBvalid(boolean bvalid) {
		this.bvalid = bvalid;
	}

}