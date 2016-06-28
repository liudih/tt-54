package forms.loyalty;

import java.util.Date;

public class BulkForm {

	Integer iid;
	
	Integer igroupid;

	Integer iwebsiteid;

	String cremark;

	Date denddate;

	Integer istatus;

	public Integer getIgroupid() {
		return igroupid;
	}

	public void setIgroupid(Integer igroupid) {
		this.igroupid = igroupid;
	}

	public Integer getIwebsiteid() {
		return iwebsiteid;
	}

	public void setIwebsiteid(Integer iwebsiteid) {
		this.iwebsiteid = iwebsiteid;
	}

	public String getCremark() {
		return cremark;
	}

	public void setCremark(String cremark) {
		this.cremark = cremark;
	}

	public Date getDenddate() {
		return denddate;
	}

	public void setDenddate(Date denddate) {
		this.denddate = denddate;
	}

	public Integer getIstatus() {
		return istatus;
	}

	public void setIstatus(Integer istatus) {
		this.istatus = istatus;
	}

	public Integer getIid() {
		return iid;
	}

	public void setIid(Integer iid) {
		this.iid = iid;
	}

}
