package values.activity.page;

import java.io.Serializable;
import java.util.Date;

public class VoteRecordQuery implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer iid;

    private String cmemberemail;
    
    private Integer ipageitemid;

    private Integer iwebsiteid;

    private String cvhost;

    private String dvotedate;
    
    private Date startDate;
    
    private Date endDate;
    
    private Integer ienable;
    
	private Integer pageSize;
	
	private Integer pageNum;
	
	private Integer votenumber;

    public Integer getIid() {
        return iid;
    }

    public void setIid(Integer iid) {
        this.iid = iid;
    }

    public String getCmemberemail() {
        return cmemberemail;
    }

    public void setCmemberemail(String cmemberemail) {
        this.cmemberemail = cmemberemail == null ? null : cmemberemail.trim();
    }

    public Integer getIpageitemid() {
        return ipageitemid;
    }

    public void setIpageitemid(Integer ipageitemid) {
        this.ipageitemid = ipageitemid;
    }

    public Integer getIwebsiteid() {
        return iwebsiteid;
    }

    public void setIwebsiteid(Integer iwebsiteid) {
        this.iwebsiteid = iwebsiteid;
    }

    public String getCvhost() {
        return cvhost;
    }

    public void setCvhost(String cvhost) {
        this.cvhost = cvhost == null ? null : cvhost.trim();
    }

    public String getDvotedate() {
        return dvotedate;
    }

    public void setDvotedate(String dvotedate) {
        this.dvotedate = dvotedate;
    }
    
	public Integer getPageSize() {
		return pageSize != null ? pageSize : 20;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public Integer getPageNum() {
		return pageNum != null ? pageNum : 1;
	}

	public void setPageNum(Integer pageNum) {
		this.pageNum = pageNum;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	
	public Integer getIenable() {
		return ienable;
	}

	public void setIenable(Integer ienable) {
		this.ienable = ienable;
	}

	@Override
	public String toString() {
		return "VoteRecordForm [iid=" + iid + ", cmemberemail=" + cmemberemail
				+ ", ipageitemid=" + ipageitemid + ", iwebsiteid=" + iwebsiteid
				+ ", cvhost=" + cvhost + ", dvotedate=" + dvotedate
				+ ", startDate=" + startDate + ", endDate=" + endDate
				+ ", ienable=" + ienable + ", pageSize=" + pageSize
				+ ", pageNum=" + pageNum + "]";
	}

	public Integer getVotenumber() {
		return votenumber;
	}

	public void setVotenumber(Integer votenumber) {
		this.votenumber = votenumber;
	}

}
