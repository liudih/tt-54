package forms.order;

import java.util.Date;

public class OrderAlterHistoryForm {
    Integer iid;

    String corderid;

    String coldvalue;

    String cnewvalue;

    String ccreateuser;

    Date dcreatedate;
    
    String cextravalue;

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
    
	public String getCextravalue() {
		return cextravalue;
	}

	public void setCextravalue(String cextravalue) {
		this.cextravalue = cextravalue;
	}

}