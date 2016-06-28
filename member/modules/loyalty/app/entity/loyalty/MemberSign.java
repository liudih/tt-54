package entity.loyalty;

import java.util.Date;


public class MemberSign {
	private Integer iid;
	private Integer iwebsiteid;
	private String  cemail;
	private Integer isigncount;	
	private Date dlastsigndate;

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

	public Integer getIsigncount() {
		return isigncount;
	}

	public void setIsigncount(Integer isigncount) {
		this.isigncount = isigncount;
	}

	public Date getDlastsigndate() {
		return dlastsigndate;
	}

	public void setDlastsigndate(Date dlastsigndate) {
		this.dlastsigndate = dlastsigndate;
	}

	public Integer getIwebsiteid() {
		return iwebsiteid;
	}

	public void setIwebsiteid(Integer iwebsiteid) {
		this.iwebsiteid = iwebsiteid;
	}
	
	
	
}
