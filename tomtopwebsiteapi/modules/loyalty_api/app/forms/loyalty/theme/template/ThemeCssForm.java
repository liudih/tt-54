package forms.loyalty.theme.template;

import java.util.Date;

public class ThemeCssForm {
    private Integer iid;

    private String cvalue;

    private String cname;

    private String ccreateuser;

    private Date dcreatedate;

    private String cupdateuser;

    private Date dupdatedate;
    
    private String tempcanme;
    
	private Integer pageSize;
	
	private Integer pageNum;

    public Integer getIid() {
        return iid;
    }

    public void setIid(Integer iid) {
        this.iid = iid;
    }

    public String getCvalue() {
        return cvalue;
    }

    public void setCvalue(String cvalue) {
        this.cvalue = cvalue == null ? null : cvalue.trim();
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname == null ? null : cname.trim();
    }

    public String getCcreateuser() {
        return ccreateuser;
    }

    public void setCcreateuser(String ccreateuser) {
        this.ccreateuser = ccreateuser == null ? null : ccreateuser.trim();
    }

    public Date getDcreatedate() {
        return dcreatedate;
    }

    public void setDcreatedate(Date dcreatedate) {
        this.dcreatedate = dcreatedate;
    }

    public String getCupdateuser() {
        return cupdateuser;
    }

    public void setCupdateuser(String cupdateuser) {
        this.cupdateuser = cupdateuser == null ? null : cupdateuser.trim();
    }

    public Date getDupdatedate() {
        return dupdatedate;
    }

    public void setDupdatedate(Date dupdatedate) {
        this.dupdatedate = dupdatedate;
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

	public String getTempcanme() {
		return tempcanme;
	}

	public void setTempcanme(String tempcanme) {
		this.tempcanme = tempcanme;
	}

	@Override
	public String toString() {
		return "ThemeCssForm [iid=" + iid + ", cvalue=" + cvalue + ", cname="
				+ cname + ", ccreateuser=" + ccreateuser + ", dcreatedate="
				+ dcreatedate + ", cupdateuser=" + cupdateuser
				+ ", dupdatedate=" + dupdatedate + ", pageSize=" + pageSize
				+ ", pageNum=" + pageNum + "]";
	}
	
	
	
}