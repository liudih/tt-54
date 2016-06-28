package forms;

import play.data.validation.Constraints.Required;

public class BannerForm {

	private Integer iid;

	@Required
	private Integer iwebsiteid;
	@Required
	private Integer ilanguageid;
	@Required
	private String ctitle;
	@Required
	private Boolean bstatus;
	@Required
	private String curl;

	private Boolean bbgimgtile;

	private String cbgcolor;

	

	public Boolean getBbgimgtile() {
		return bbgimgtile==null?false:true;
	}

	public void setBbgimgtile(Boolean bbgimgtile) {
		this.bbgimgtile = bbgimgtile;
	}

	public String getCbgcolor() {
		return cbgcolor;
	}

	public void setCbgcolor(String cbgcolor) {
		this.cbgcolor = cbgcolor;
	}

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

	public Integer getIlanguageid() {
		return ilanguageid;
	}

	public void setIlanguageid(Integer ilanguageid) {
		this.ilanguageid = ilanguageid;
	}

	public String getCtitle() {
		return ctitle;
	}

	public void setCtitle(String ctitle) {
		this.ctitle = ctitle;
	}

	public Boolean getBstatus() {
		return bstatus;
	}

	public void setBstatus(Boolean bstatus) {
		this.bstatus = bstatus;
	}

	public String getCurl() {
		return curl;
	}

	public void setCurl(String curl) {
		this.curl = curl;
	}

}
