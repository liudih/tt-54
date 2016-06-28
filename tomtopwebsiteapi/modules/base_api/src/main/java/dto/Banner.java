package dto;

import java.io.Serializable;
import java.sql.Date;


public class Banner implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer iid;
	
    private Integer iwebsiteid;
    
    private Integer ilanguageid;
   
	private String ctitle;
	
	private Boolean bstatus;
		
	private String curl;	
	
    private Integer iindex;
    
	private String  cbgcolor;
	
	private Date dcreatedate;
	
    private Boolean bhasbgimage;
    
	private Boolean bbgimgtile;

	private String website;
	
	private String language;
	
	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
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

	public Integer getIindex() {
		return iindex;
	}

	public void setIindex(Integer iindex) {
		this.iindex = iindex;
	}

	public String getCbgcolor() {
		return cbgcolor;
	}

	public void setCbgcolor(String cbgcolor) {
		
		this.cbgcolor = cbgcolor==null?"#fff":cbgcolor;
	}

	public Date getDcreatedate() {
		return dcreatedate;
	}

	public void setDcreatedate(Date dcreatedate) {
		this.dcreatedate = dcreatedate;
	}

	public Boolean getBhasbgimage() {
		return bhasbgimage;
	}

	public void setBhasbgimage(Boolean bhasbgimage) {
		this.bhasbgimage = bhasbgimage;
	}

	public Boolean getBbgimgtile() {
		return bbgimgtile;
	}

	public void setBbgimgtile(Boolean bbgimgtile) {
		this.bbgimgtile = bbgimgtile;
	}
	
	
	
	
	
}
