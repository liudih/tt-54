package dto.product;

import java.io.Serializable;

public class CategoryLabelName implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer iid;

    private Integer icategorylabelid;

    private Integer ilanguageid;

    private String curl;

    private String cprompt;

    private String ccreateuser;

    private byte[] cimages;

    public Integer getIid() {
        return iid;
    }

    public void setIid(Integer iid) {
        this.iid = iid;
    }

    public Integer getIcategorylabelid() {
        return icategorylabelid;
    }

    public void setIcategorylabelid(Integer icategorylabelid) {
        this.icategorylabelid = icategorylabelid;
    }

    public Integer getIlanguageid() {
        return ilanguageid;
    }

    public void setIlanguageid(Integer ilanguageid) {
        this.ilanguageid = ilanguageid;
    }

    public String getCurl() {
        return curl;
    }

    public void setCurl(String curl) {
        this.curl = curl == null ? null : curl.trim();
    }

    public String getCprompt() {
        return cprompt;
    }

    public void setCprompt(String cprompt) {
        this.cprompt = cprompt == null ? null : cprompt.trim();
    }

    public String getCcreateuser() {
        return ccreateuser;
    }

    public void setCcreateuser(String ccreateuser) {
        this.ccreateuser = ccreateuser == null ? null : ccreateuser.trim();
    }

    public byte[] getCimages() {
        return cimages;
    }

    public void setCimages(byte[] cimages) {
        this.cimages = cimages;
    }
}