package dto.advertising;

import java.util.Date;

public class AdvertisingBase {
    private Long iid;

    private String cimageurl;
    
    private Integer iposition; //方位，如：1代表right 2代表left

    private String ccreateuser;

    private Date dcreatedate;

    private String clastupdateduser;

    private Date dlastupdateddate;
 
    
    public Integer getIposition() {
		return iposition;
	}

	public void setIposition(Integer iposition) {
		this.iposition = iposition;
	}
	 
	public Long getIid() {
        return iid;
    }

    public void setIid(Long iid) {
        this.iid = iid;
    }

    public String getCimageurl() {
        return cimageurl;
    }

    public void setCimageurl(String cimageurl) {
        this.cimageurl = cimageurl == null ? null : cimageurl.trim();
    }

//    public String getCurl() {
//        return curl;
//    }
//
//    public void setCurl(String curl) {
//        this.curl = curl == null ? null : curl.trim();
//    }

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