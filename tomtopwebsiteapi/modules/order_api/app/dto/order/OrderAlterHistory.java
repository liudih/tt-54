package dto.order;

import java.io.Serializable;
import java.util.Date;

public class OrderAlterHistory implements Serializable {
    private Integer iid;

    private String corderid;

    private String coldvalue;

    private String cnewvalue;

    private String ccreateuser;

    private Date dcreatedate;

    public Integer getIid() {
        return iid;
    }

    public void setIid(Integer iid) {
        this.iid = iid;
    }

    public String getCorderid() {
        return corderid;
    }

    public void setCorderid(String corderid) {
        this.corderid = corderid == null ? null : corderid.trim();
    }

    public String getColdvalue() {
        return coldvalue;
    }

    public void setColdvalue(String coldvalue) {
        this.coldvalue = coldvalue == null ? null : coldvalue.trim();
    }

    public String getCnewvalue() {
        return cnewvalue;
    }

    public void setCnewvalue(String cnewvalue) {
        this.cnewvalue = cnewvalue == null ? null : cnewvalue.trim();
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

	@Override
	public String toString() {
		return "OrderAlterHistory [iid=" + iid + ", corderid=" + corderid
				+ ", coldvalue=" + coldvalue + ", cnewvalue=" + cnewvalue
				+ ", ccreateuser=" + ccreateuser + ", dcreatedate="
				+ dcreatedate + "]";
	}
}