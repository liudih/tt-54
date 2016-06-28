package entity.base;

import java.sql.Date;

public class Banner {

	

	private Integer iid;
	
    private Integer iwebsiteid;
	
    private Integer ilanguageid;
    
	private String ctitle;
	
	private Boolean bstatus;
			
	private byte[]  bfile;
	
	private byte[]  bbgimagefile;
	
	private String  cbgcolor;
	
	private String curl;	
	
	private Integer iindex;
	
	private Date dcreatedate;
	
	private Boolean bhasbgimage;
	
	private Boolean bbgimgtile;

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

	public byte[] getBfile() {
		return bfile;
	}

	public void setBfile(byte[] bfile) {
		this.bfile = bfile;
	}

	public byte[] getBbgimagefile() {
		return bbgimagefile;
	}

	public void setBbgimagefile(byte[] bbgimagefile) {
		this.bbgimagefile = bbgimagefile;
	}

	public String getCbgcolor() {
		return cbgcolor;
	}

	public void setCbgcolor(String cbgcolor) {
		this.cbgcolor = cbgcolor;
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
