package dto.member;

import java.io.Serializable;
import java.util.Date;

public class MemberGroup implements Serializable {
	private static final long serialVersionUID = 1L;
	
    private Integer iid;

    private String cgroupname;

    private Integer itaxclassid;

    private String ccreateuser;

    private Date dcreatedate;

    public Integer getIid() {
        return iid;
    }

    public void setIid(Integer iid) {
        this.iid = iid;
    }

    public String getCgroupname() {
        return cgroupname;
    }

    public void setCgroupname(String cgroupname) {
        this.cgroupname = cgroupname == null ? null : cgroupname.trim();
    }

    public Integer getItaxclassid() {
        return itaxclassid;
    }

    public void setItaxclassid(Integer itaxclassid) {
        this.itaxclassid = itaxclassid;
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