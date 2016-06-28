package dto.product;

import java.util.Date;

public class ProductAttachmentMapper {
    private Integer iid;

    private Integer iwebsiteid;

    private String csku;

    private Integer ilanguage;

    private String clistingid;

    private Integer iattachmentdescid;
    
    private String ccreateuser;

    private Date dcreatedate;
    
    public Integer getIid() {
        return iid;
    }

    public void setIid(Integer iid) {
        this.iid = iid;
    }

    public Integer getIwebsiteid() {
        return iwebsiteid;
    }

    public void setIwebsiteid(Integer iwebsiteid) {
        this.iwebsiteid = iwebsiteid;
    }

    public String getCsku() {
        return csku;
    }

    public void setCsku(String csku) {
        this.csku = csku == null ? null : csku.trim();
    }

    public Integer getIlanguage() {
        return ilanguage;
    }

    public void setIlanguage(Integer ilanguage) {
        this.ilanguage = ilanguage;
    }

    public String getClistingid() {
        return clistingid;
    }

    public void setClistingid(String clistingid) {
        this.clistingid = clistingid == null ? null : clistingid.trim();
    }

    public Integer getIattachmentdescid() {
        return iattachmentdescid;
    }

    public void setIattachmentdescid(Integer iattachmentdescid) {
        this.iattachmentdescid = iattachmentdescid;
    }

	public String getCcreateuser() {
		return ccreateuser;
	}

	public void setCcreateuser(String ccreateuser) {
		this.ccreateuser = ccreateuser;
	}

	public Date getDcreatedate() {
		return dcreatedate;
	}

	public void setDcreatedate(Date dcreatedate) {
		this.dcreatedate = dcreatedate;
	}
    
}