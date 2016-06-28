package dto.product;

import java.util.Date;

public class AttachmentDesc {
    private Integer iid;

    private Integer ilanguage;

    private String ctitle;

    private String cdescribe;

    private Integer iattachmentid;

    private String ccreateuser;

    private Date dcreatedate;

    private String cupdateuser;

    private Date dupdatedate;
    
    public AttachmentDesc() {
		super();
	}

	public AttachmentDesc(Integer iattachmentid) {
		super();
		this.iattachmentid = iattachmentid;
	}

	public Integer getIid() {
        return iid;
    }

    public void setIid(Integer iid) {
        this.iid = iid;
    }

    public Integer getIlanguage() {
        return ilanguage;
    }

    public void setIlanguage(Integer ilanguage) {
        this.ilanguage = ilanguage;
    }

    public String getCtitle() {
        return ctitle;
    }

    public void setCtitle(String ctitle) {
        this.ctitle = ctitle == null ? null : ctitle.trim();
    }

    public String getCdescribe() {
        return cdescribe;
    }

    public void setCdescribe(String cdescribe) {
        this.cdescribe = cdescribe == null ? null : cdescribe.trim();
    }

    public Integer getIattachmentid() {
		return iattachmentid;
	}

	public void setIattachmentid(Integer iattachmentid) {
		this.iattachmentid = iattachmentid;
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

    public String getCupdateuser() {
        return cupdateuser;
    }

    public void setCupdateuser(String cupdateuser) {
        this.cupdateuser = cupdateuser == null ? null : cupdateuser.trim();
    }

    public Date getDupdatedate() {
        return dupdatedate;
    }

    public void setDupdatedate(Date dupdatedate) {
        this.dupdatedate = dupdatedate;
    }
}