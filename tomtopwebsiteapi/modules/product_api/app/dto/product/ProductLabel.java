package dto.product;

import java.io.Serializable;
import java.util.Date;
import services.base.utils.DateFormatUtils;


public class ProductLabel implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Integer iid;
	Integer iwebsiteid;
	String ctype;
	String clistingid;
	Date   dcreatedate;
	String ccreateuser;

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

	public String getCtype() {
		return ctype;
	}

	public void setCtype(String ctype) {
		this.ctype = ctype;
	}

	public String getClistingid() {
		return clistingid;
	}

	public void setClistingid(String clistingid) {
		this.clistingid = clistingid;
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

	public String getDcreatedateString() {
		return DateFormatUtils.getStrFromYYYYMMDDHHMMSS(this.dcreatedate);

	}
}
