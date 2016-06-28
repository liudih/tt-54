package dto.product;

import java.io.Serializable;

public class ProductMultiattributeSku implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer iid;

    private String cparentsku;

    private String csku;

    public Integer getIid() {
        return iid;
    }

    public void setIid(Integer iid) {
        this.iid = iid;
    }

    public String getCparentsku() {
        return cparentsku;
    }

    public void setCparentsku(String cparentsku) {
        this.cparentsku = cparentsku == null ? null : cparentsku.trim();
    }

    public String getCsku() {
        return csku;
    }

    public void setCsku(String csku) {
        this.csku = csku == null ? null : csku.trim();
    }
}