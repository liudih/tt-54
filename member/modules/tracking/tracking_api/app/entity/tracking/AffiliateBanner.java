package entity.tracking;

import java.util.Date;

public class AffiliateBanner {
    private Integer iid;

    private Integer iwebsiteid;

    private String ctitle;

    private String clink;

    private String cbannertype;

    private Integer iwidth;

    private Integer iheight;

    private String ctarget;

    private Boolean bstatus;

    private Boolean brelnofollow;

    private Date dcreatedate;

    private byte[] bbannerfile;
    
    private Integer icategoryid;
    
    private String categoryName;
    
    private String cdescription;
    
    public String getCdescription() {
		return cdescription;
	}

	public void setCdescription(String cdescription) {
		this.cdescription = cdescription;
	}

	public Integer getIcategoryid() {
		return icategoryid;
	}

	public void setIcategoryid(Integer icategoryid) {
		this.icategoryid = icategoryid;
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

    public String getCtitle() {
        return ctitle;
    }

    public void setCtitle(String ctitle) {
        this.ctitle = ctitle == null ? null : ctitle.trim();
    }

    public String getClink() {
        return clink;
    }

    public void setClink(String clink) {
        this.clink = clink == null ? null : clink.trim();
    }

    public String getCbannertype() {
        return cbannertype;
    }

    public void setCbannertype(String cbannertype) {
        this.cbannertype = cbannertype == null ? null : cbannertype.trim();
    }

    public Integer getIwidth() {
        return iwidth;
    }

    public void setIwidth(Integer iwidth) {
        this.iwidth = iwidth;
    }

    public Integer getIheight() {
        return iheight;
    }

    public void setIheight(Integer iheight) {
        this.iheight = iheight;
    }

    public String getCtarget() {
        return ctarget;
    }

    public void setCtarget(String ctarget) {
        this.ctarget = ctarget == null ? null : ctarget.trim();
    }

    public Boolean getBstatus() {
        return bstatus;
    }

    public void setBstatus(Boolean bstatus) {
        this.bstatus = bstatus;
    }

    public Boolean getBrelnofollow() {
        return brelnofollow;
    }

    public void setBrelnofollow(Boolean brelnofollow) {
        this.brelnofollow = brelnofollow;
    }

    public Date getDcreatedate() {
        return dcreatedate;
    }

    public void setDcreatedate(Date dcreatedate) {
        this.dcreatedate = dcreatedate;
    }

    public byte[] getBbannerfile() {
        return bbannerfile;
    }

    public void setBbannerfile(byte[] bbannerfile) {
        this.bbannerfile = bbannerfile;
    }

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
}