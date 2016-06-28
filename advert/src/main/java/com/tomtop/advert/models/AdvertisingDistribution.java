package com.tomtop.advert.models;

import java.util.Date;

public class AdvertisingDistribution {
    private Long iid;

    private Long iadvertisingid;
    
    private String cbusinessid;
    
    private Integer iwebsiteid;
    
    private Integer icategoryid;

    private String ccreateuser;

    private Date dcreatedate;

    private String clastupdateduser;

    private Date dlastupdateddate;
 
    private Integer itype;
    
    private String websitename;
    
    private String advertisingtypename;
    
    private String cdevice;
    

	public String getCdevice() {
		return cdevice;
	}

	public void setCdevice(String cdevice) {
		this.cdevice = cdevice;
	}

	public String getWebsitename() {
		return websitename;
	}

	public void setWebsitename(String websitename) {
		this.websitename = websitename;
	}

	public String getAdvertisingtypename() {
		return advertisingtypename;
	}

	public void setAdvertisingtypename(String advertisingtypename) {
		this.advertisingtypename = advertisingtypename;
	}

	public Integer getItype() {
		return itype;
	}

	public void setItype(Integer itype) {
		this.itype = itype;
	}

	public Integer getIcategoryid() {
		return icategoryid;
	}

	public void setIcategoryid(Integer icategoryid) {
		this.icategoryid = icategoryid;
	}

	public String getCbusinessid() {
		return cbusinessid;
	}

	public void setCbusinessid(String cbusinessid) {
		this.cbusinessid = cbusinessid;
	}

	public Integer getIwebsiteid() {
		return iwebsiteid;
	}

	public void setIwebsiteid(Integer iwebsiteid) {
		this.iwebsiteid = iwebsiteid;
	}

	public Long getIid() {
        return iid;
    }

    public void setIid(Long iid) {
        this.iid = iid;
    }

  

//    public String getClistingid() {
//        return clistingid;
//    }
//
//    public void setClistingid(String clistingid) {
//        this.clistingid = clistingid == null ? null : clistingid.trim();
//    }

    public Long getIadvertisingid() {
		return iadvertisingid;
	}

	public void setIadvertisingid(Long iadvertisingid) {
		this.iadvertisingid = iadvertisingid;
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

    public String getClastupdateduser() {
        return clastupdateduser;
    }

    public void setClastupdateduser(String clastupdateduser) {
        this.clastupdateduser = clastupdateduser == null ? null : clastupdateduser.trim();
    }

    public Date getDlastupdateddate() {
        return dlastupdateddate;
    }

    public void setDlastupdateddate(Date dlastupdateddate) {
        this.dlastupdateddate = dlastupdateddate;
    }
}