package dto.cart;

import java.io.Serializable;
import java.util.Date;

public class CartHistory implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private Integer iid;

    private String cip;

    private String cmemberemail;

    private String clistingid;

    private Date dcreatedate;

    private Integer itype;

    public Integer getIid() {
        return iid;
    }

    public void setIid(Integer iid) {
        this.iid = iid;
    }

    public String getCip() {
        return cip;
    }

    public void setCip(String cip) {
        this.cip = cip == null ? null : cip.trim();
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

    public Date getDcreatedate() {
        return dcreatedate;
    }

    public void setDcreatedate(Date dcreatedate) {
        this.dcreatedate = dcreatedate;
    }

    public Integer getItype() {
        return itype;
    }

    public void setItype(Integer itype) {
        this.itype = itype;
    }
}