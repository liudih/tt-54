package entity.base;

import java.util.Date;

public class Shorturl {
	Integer iid;
	String curl;
	String cshorturl;
	String cshorturlcode;
	Date dcreatedate;
	String caid;
	String ceid;
	Integer itasktype;
	public Integer getIid() {
		return iid;
	}
	public void setIid(Integer iid) {
		this.iid = iid;
	}
	public String getCurl() {
		return curl;
	}
	public void setCurl(String curl) {
		this.curl = curl;
	}
	public String getCshorturl() {
		return cshorturl;
	}
	public void setCshorturl(String cshorturl) {
		this.cshorturl = cshorturl;
	}
	public Date getDcreatedate() {
		return dcreatedate;
	}
	public void setDcreatedate(Date dcreatedate) {
		this.dcreatedate = dcreatedate;
	}
	public String getCaid() {
		return caid;
	}
	public void setCaid(String caid) {
		this.caid = caid;
	}
	public String getCeid() {
		return ceid;
	}
	public void setCeid(String ceid) {
		this.ceid = ceid;
	}
	public String getCshorturlcode() {
		return cshorturlcode;
	}
	public void setCshorturlcode(String cshorturlcode) {
		this.cshorturlcode = cshorturlcode;
	}
	public Integer getItasktype() {
		return itasktype;
	}
	public void setItasktype(Integer itasktype) {
		this.itasktype = itasktype;
	}
}
