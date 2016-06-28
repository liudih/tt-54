package entity.attribute;

import java.util.Date;

public class AttributeValue {
    private Integer ivalueid;

    private Integer ikeyid;

    private String ccreateuser;

    private Date dcreatedate;

    public Integer getIvalueid() {
        return ivalueid;
    }

    public void setIvalueid(Integer ivalueid) {
        this.ivalueid = ivalueid;
    }

    public Integer getIkeyid() {
        return ikeyid;
    }

    public void setIkeyid(Integer ikeyid) {
        this.ikeyid = ikeyid;
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