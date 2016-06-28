package entity.loyalty;

import java.util.Date;

public class Bulk {
    private Integer iid;

    private Integer igroupid;

    private Integer iwebsiteid;

    private String cremark;

    private Date dcreatedate;

    private Date denddate;

    private Integer istatus;
    
    private String groupName;
    
    private String websiteName;

    public Integer getIid() {
        return iid;
    }

    public void setIid(Integer iid) {
        this.iid = iid;
    }

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
        this.cremark = cremark == null ? null : cremark.trim();
    }

    public Date getDcreatedate() {
        return dcreatedate;
    }

    public void setDcreatedate(Date dcreatedate) {
        this.dcreatedate = dcreatedate;
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

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getWebsiteName() {
		return websiteName;
	}

	public void setWebsiteName(String websiteName) {
		this.websiteName = websiteName;
	}
}