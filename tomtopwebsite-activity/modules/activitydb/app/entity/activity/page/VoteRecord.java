package entity.activity.page;

import java.util.Date;

public class VoteRecord {
    private Integer iid;

    private String cmemberemail;

    private Integer ipageitemid;

    private Integer iwebsiteid;

    private String cvhost;

    private Date dvotedate;

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

    public Date getDvotedate() {
        return dvotedate;
    }

    public void setDvotedate(Date dvotedate) {
        this.dvotedate = dvotedate;
    }
}