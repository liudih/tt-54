package entity.tracking;

import java.util.Date;

public class AffiliateInfo {

	private Integer iid;

    private Integer iwebsiteid;

    private String cemail;

    private String caid;

    private Integer isalerid;

    private String cpaypalemail;

    private Boolean breceivenotification;

    private Boolean bstatus;

    private Boolean bexternal;

    private Date dcreatedate;
    
    private String salerName;

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

    public String getCemail() {
        return cemail;
    }

    public void setCemail(String cemail) {
        this.cemail = cemail == null ? null : cemail.trim();
    }

    public String getCaid() {
        return caid;
    }

    public void setCaid(String caid) {
        this.caid = caid == null ? null : caid.trim();
    }

    public Integer getIsalerid() {
        return isalerid;
    }

    public void setIsalerid(Integer isalerid) {
        this.isalerid = isalerid;
    }

    public String getCpaypalemail() {
        return cpaypalemail;
    }

    public void setCpaypalemail(String cpaypalemail) {
        this.cpaypalemail = cpaypalemail == null ? null : cpaypalemail.trim();
    }

    public Boolean getBreceivenotification() {
        return breceivenotification;
    }

    public void setBreceivenotification(Boolean breceivenotification) {
        this.breceivenotification = breceivenotification;
    }

    public Boolean getBstatus() {
        return bstatus;
    }

    public void setBstatus(Boolean bstatus) {
        this.bstatus = bstatus;
    }

    public Boolean getBexternal() {
        return bexternal;
    }

    public void setBexternal(Boolean bexternal) {
        this.bexternal = bexternal;
    }

    public Date getDcreatedate() {
        return dcreatedate;
    }

    public void setDcreatedate(Date dcreatedate) {
        this.dcreatedate = dcreatedate;
    }

	public String getSalerName() {
		return salerName;
	}

	public void setSalerName(String salerName) {
		this.salerName = salerName;
	}

	@Override
	public String toString() {
		return "AffiliateInfo [iid=" + iid + ", iwebsiteid=" + iwebsiteid
				+ ", cemail=" + cemail + ", caid=" + caid + ", isalerid="
				+ isalerid + ", cpaypalemail=" + cpaypalemail
				+ ", breceivenotification=" + breceivenotification
				+ ", bstatus=" + bstatus + ", bexternal=" + bexternal
				+ ", dcreatedate=" + dcreatedate + ", salerName=" + salerName
				+ "]";
	}
	
	
}