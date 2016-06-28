package dto.cart;

import java.io.Serializable;
import java.util.Date;

public class CartItemHistory implements Serializable {
	private static final long serialVersionUID = 1L;
    private Integer iid;

    private String cuuid;

    private String cmemberemail;

    private String clistingid;

    private String ccreateuser;

    private Date dcreatedate;

    private Date dadddate;

    private Date dupdatedate;

    public Integer getIid() {
        return iid;
    }

    public void setIid(Integer iid) {
        this.iid = iid;
    }

    public String getCuuid() {
        return cuuid;
    }

    public void setCuuid(String cuuid) {
        this.cuuid = cuuid == null ? null : cuuid.trim();
    }

    public String getCmemberemail() {
        return cmemberemail;
    }

    public void setCmemberemail(String cmemberemail) {
        this.cmemberemail = cmemberemail == null ? null : cmemberemail.trim();
    }

    public String getClistingid() {
        return clistingid;
    }

    public void setClistingid(String clistingid) {
        this.clistingid = clistingid == null ? null : clistingid.trim();
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

    public Date getDadddate() {
        return dadddate;
    }

    public void setDadddate(Date dadddate) {
        this.dadddate = dadddate;
    }

    public Date getDupdatedate() {
        return dupdatedate;
    }

    public void setDupdatedate(Date dupdatedate) {
        this.dupdatedate = dupdatedate;
    }
}