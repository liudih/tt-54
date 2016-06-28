package entity.tracking;

import java.util.Date;

public class VisitLog {
	public String caid;
	public Integer iwebsiteid;
	public String cip;
	public String cpath;
	public String csource;
	public Date dcreatedate;
	public String ceid;
	public Integer itasktype;

	public String saler;
	public String getCaid() {
		return caid;
	}

	public void setCaid(String caid) {
		this.caid = caid;
	}

	public String getCip() {
		return cip;
	}

	public void setCip(String cip) {
		this.cip = cip;
	}

	public String getCsource() {
		return csource;
	}

	public void setCsource(String csource) {
		this.csource = csource;
	}

	public String getCpath() {
		return cpath;
	}

	public void setCpath(String cpath) {
		this.cpath = cpath;
	}

	public Date getDcreateDate() {
		return dcreatedate;
	}

	public void setDcreateDate(Date dcreateDate) {
		this.dcreatedate = dcreateDate;
	}

	public Integer getIwebsiteid() {
		return iwebsiteid;
	}

	public void setIwebsiteid(Integer iwebsiteid) {
		this.iwebsiteid = iwebsiteid;
	}

	public String getCeid() {
		return ceid;
	}

	public void setCeid(String ceid) {
		this.ceid = ceid;
	}

	public String getSaler() {
		return saler;
	}

	public void setSaler(String saler) {
		this.saler = saler;
	}

	public Integer getItasktype() {
		return itasktype;
	}

	public void setItasktype(Integer itasktype) {
		this.itasktype = itasktype;
	}

}
