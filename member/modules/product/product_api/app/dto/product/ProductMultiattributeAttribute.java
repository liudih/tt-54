package dto.product;

import java.io.Serializable;

public class ProductMultiattributeAttribute implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer iid;

    private Integer iwebsiteid;

    private String cparentsku;

    private String ckey;
    
    private Integer ikey;

    private Boolean bshowimg;

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

    public String getCparentsku() {
        return cparentsku;
    }

    public void setCparentsku(String cparentsku) {
        this.cparentsku = cparentsku == null ? null : cparentsku.trim();
    }

    public String getCkey() {
        return ckey;
    }

    public void setCkey(String ckey) {
        this.ckey = ckey == null ? null : ckey.trim();
    }

    public Boolean getBshowimg() {
        return bshowimg;
    }

    public void setBshowimg(Boolean bshowimg) {
        this.bshowimg = bshowimg;
    }

	public Integer getIkey() {
		return ikey;
	}

	public void setIkey(Integer ikey) {
		this.ikey = ikey;
	}
}