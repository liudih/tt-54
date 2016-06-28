package dto.product;

import java.io.Serializable;
import java.util.Date;

public class ProductSalePrice implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer iid;

    private String clistingid;

    private String csku;

    private Double fsaleprice;

    private Date dbegindate;

    private Date denddate;

    private String ccreateuser;

    private Date dcreatedate;

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

    public Double getFsaleprice() {
        return fsaleprice;
    }

    public void setFsaleprice(Double fsaleprice) {
        this.fsaleprice = fsaleprice;
    }

    public Date getDbegindate() {
        return dbegindate;
    }

    public void setDbegindate(Date dbegindate) {
        this.dbegindate = dbegindate;
    }

    public Date getDenddate() {
        return denddate;
    }

    public void setDenddate(Date denddate) {
        this.denddate = denddate;
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

	@Override
	public String toString() {
		return "ProductSalePrice [iid=" + iid + ", clistingid=" + clistingid
				+ ", csku=" + csku + ", fsaleprice=" + fsaleprice
				+ ", dbegindate=" + dbegindate + ", denddate=" + denddate
				+ ", ccreateuser=" + ccreateuser + ", dcreatedate="
				+ dcreatedate + "]";
	}
    
    
}