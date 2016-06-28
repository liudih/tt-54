package entity.member;

import java.util.Date;

public class MemberOtherId {

	Integer iid;
	String cemail;
	String csource;
	String csourceid;
	Date dcreatedate;
	Boolean bvalidated;

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

	public String getCsource() {
		return csource;
	}

	public void setCsource(String csource) {
		this.csource = csource;
	}

	public String getCsourceid() {
		return csourceid;
	}

	public void setCsourceid(String csourceid) {
		this.csourceid = csourceid;
	}

	public Date getDcreatedate() {
		return dcreatedate;
	}

	public void setDcreatedate(Date dcreatedate) {
		this.dcreatedate = dcreatedate;
	}

	public Boolean getBvalidated() {
		return bvalidated;
	}

	public void setBvalidated(Boolean bvalidated) {
		this.bvalidated = bvalidated;
	}

}
