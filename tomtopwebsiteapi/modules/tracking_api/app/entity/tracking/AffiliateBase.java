package entity.tracking;

import java.util.Date;

public class AffiliateBase {
    private String caid;

    private Integer iwebsiteid;

    private Integer itype;

    private String ccreateuser;

    private Date dcreatedate;

    public String getCaid() {
        return caid;
    }

    public void setCaid(String caid) {
        this.caid = caid == null ? null : caid.trim();
    }

    @Override
	public String toString() {
		return "AffiliateBase [caid=" + caid + ", iwebsiteid=" + iwebsiteid
				+ ", itype=" + itype + ", ccreateuser=" + ccreateuser
				+ ", dcreatedate=" + dcreatedate + "]";
	}

	public Integer getIwebsiteid() {
        return iwebsiteid;
    }

    public void setIwebsiteid(Integer iwebsiteid) {
        this.iwebsiteid = iwebsiteid;
    }

    public Integer getItype() {
        return itype;
    }

    public void setItype(Integer itype) {
        this.itype = itype;
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
}