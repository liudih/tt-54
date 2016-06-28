package dto.topic;

import java.io.Serializable;
import java.util.Date;

public class TopicPage implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer iid;
	private Integer iwebsiteid;
	private Integer ilanguageid;
	private String ctype;
	private String ctitle;
	private byte[] cimage;
	private String chtmlurl;
	private Boolean bshow;
	private Integer icreateuserid;
	private Date dcreatedate;

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

	public String getCtype() {
		return ctype;
	}

	public void setCtype(String ctype) {
		this.ctype = ctype;
	}

	public String getCtitle() {
		return ctitle;
	}

	public void setCtitle(String ctitle) {
		this.ctitle = ctitle;
	}

	public byte[] getCimage() {
		return cimage;
	}

	public void setCimage(byte[] cimage) {
		this.cimage = cimage;
	}

	public String getChtmlurl() {
		return chtmlurl;
	}

	public void setChtmlurl(String chtmlurl) {
		this.chtmlurl = chtmlurl;
	}

	public Boolean getBshow() {
		return bshow;
	}

	public void setBshow(Boolean bshow) {
		this.bshow = bshow;
	}

	public Date getDcreatedate() {
		return dcreatedate;
	}

	public void setDcreatedate(Date dcreatedate) {
		this.dcreatedate = dcreatedate;
	}

	public Integer getIcreateuserid() {
		return icreateuserid;
	}

	public void setIcreateuserid(Integer icreateuserid) {
		this.icreateuserid = icreateuserid;
	}

	@Override
	public String toString() {
		return "TopicPage [iid=" + iid + ", iwebsiteid=" + iwebsiteid
				+ ", ilanguageid=" + ilanguageid + ", ctype=" + ctype
				+ ", ctitle=" + ctitle + ", chtmlurl=" + chtmlurl + ", bshow="
				+ bshow + ", icreateuserid=" + icreateuserid + ", dcreatedate="
				+ dcreatedate + "]";
	}

}
