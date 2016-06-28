package entity.activity.page;

public class PageItem {
    private Integer iid;

    private Integer ipageid;

    private String cvalue;

    private String cimgurl;

    private String cimgtargeturl;
    
    private String pageItemName;
    
    private Integer ipriority;

    public Integer getIid() {
        return iid;
    }

    public void setIid(Integer iid) {
        this.iid = iid;
    }

    public Integer getIpageid() {
        return ipageid;
    }

    public void setIpageid(Integer ipageid) {
        this.ipageid = ipageid;
    }

    public String getCvalue() {
        return cvalue;
    }

    public void setCvalue(String cvalue) {
        this.cvalue = cvalue == null ? null : cvalue.trim();
    }

    public String getCimgurl() {
        return cimgurl;
    }

    public void setCimgurl(String cimgurl) {
        this.cimgurl = cimgurl == null ? null : cimgurl.trim();
    }

    public String getCimgtargeturl() {
        return cimgtargeturl;
    }

    public void setCimgtargeturl(String cimgtargeturl) {
        this.cimgtargeturl = cimgtargeturl == null ? null : cimgtargeturl.trim();
    }

	public String getPageItemName() {
		return pageItemName;
	}

	public void setPageItemName(String pageItemName) {
		this.pageItemName = pageItemName;
	}

	public Integer getIpriority() {
		return ipriority;
	}

	public void setIpriority(Integer ipriority) {
		this.ipriority = ipriority;
	}
	
}