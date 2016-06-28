package dto.interaction;

import java.io.Serializable;
import java.util.Date;

public class InteractionProductMemberVideo implements Serializable  {
	private static final long serialVersionUID = 1L;
    private Integer iid;

    private String clistingid;

    private String csku;

    private String cmemberemail;

    private Integer icomment;

    private String cvideourl;

    private String clabel;

    private Date dcreatedate;

    private String cauditoruser;

    private Date dauditordate;

    private Integer iauditorstatus;
    
    private Integer iwebsiteid;

    public Integer getIid() {
        return iid;
    }

    public void setIid(Integer iid) {
        this.iid = iid;
    }

    public String getClistingid() {
        return clistingid;
    }

    public void setClistingid(String clistingid) {
        this.clistingid = clistingid == null ? null : clistingid.trim();
    }

    public String getCsku() {
        return csku;
    }

    public void setCsku(String csku) {
        this.csku = csku == null ? null : csku.trim();
    }
    
	public String getCmemberemail() {
		return cmemberemail;
	}

	public void setCmemberemail(String cmemberemail) {
		this.cmemberemail = cmemberemail;
	}

	public Integer getIcomment() {
        return icomment;
    }

    public void setIcomment(Integer icomment) {
        this.icomment = icomment;
    }

    public String getCvideourl() {
        return cvideourl;
    }

    public void setCvideourl(String cvideourl) {
        this.cvideourl = cvideourl == null ? null : cvideourl.trim();
    }

    public String getClabel() {
        return clabel;
    }

    public void setClabel(String clabel) {
        this.clabel = clabel == null ? null : clabel.trim();
    }

    public Date getDcreatedate() {
        return dcreatedate;
    }

    public void setDcreatedate(Date dcreatedate) {
        this.dcreatedate = dcreatedate;
    }

    public String getCauditoruser() {
        return cauditoruser;
    }

    public void setCauditoruser(String cauditoruser) {
        this.cauditoruser = cauditoruser == null ? null : cauditoruser.trim();
    }

    public Date getDauditordate() {
        return dauditordate;
    }

    public void setDauditordate(Date dauditordate) {
        this.dauditordate = dauditordate;
    }

    public Integer getIauditorstatus() {
        return iauditorstatus;
    }

    public void setIauditorstatus(Integer iauditorstatus) {
        this.iauditorstatus = iauditorstatus;
    }

	public Integer getIwebsiteid() {
		return iwebsiteid;
	}

	public void setIwebsiteid(Integer iwebsiteid) {
		this.iwebsiteid = iwebsiteid;
	}
}