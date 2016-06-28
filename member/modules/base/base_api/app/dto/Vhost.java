package dto;

import java.io.Serializable;
import java.util.Date;

public class Vhost implements Serializable {
	private static final long serialVersionUID = 1L;
	
	String cvhost;
	String iwebsiteid;
	Integer ilanguageid;
	Integer icurrencyid;
	String ccreateuser;
	Date dcreatedate;
	String cdevice;
	public String getCvhost() {
		return cvhost;
	}
	public void setCvhost(String cvhost) {
		this.cvhost = cvhost;
	}
	public String getIwebsiteid() {
		return iwebsiteid;
	}
	public void setIwebsiteid(String iwebsiteid) {
		this.iwebsiteid = iwebsiteid;
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
	public String getCdevice() {
		return cdevice;
	}
	public void setCdevice(String cdevice) {
		this.cdevice = cdevice;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
