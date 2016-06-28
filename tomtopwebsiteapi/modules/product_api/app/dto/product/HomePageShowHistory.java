package dto.product;

import java.util.Date;

public class HomePageShowHistory {
	private Integer iid;

	private Integer iwebsiteid;

	private String ctype;

	private String clistingid;

	private Date dshowtime;

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
		this.ctype = ctype == null ? null : ctype.trim();
	}

	public String getClistingid() {
		return clistingid;
	}

	public void setClistingid(String clistingid) {
		this.clistingid = clistingid == null ? null : clistingid.trim();
	}

	public Date getDshowtime() {
		return dshowtime;
	}

	public void setDshowtime(Date dshowtime) {
		this.dshowtime = dshowtime;
	}
}