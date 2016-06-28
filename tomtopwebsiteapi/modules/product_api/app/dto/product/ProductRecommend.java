package dto.product;

import java.io.Serializable;
import java.util.Date;

public class ProductRecommend implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer iid;

    private String clistingid;

    private String csku;

    private String crecommendlisting;

    private String crecommendsku;

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

    public String getCrecommendlisting() {
        return crecommendlisting;
    }

    public void setCrecommendlisting(String crecommendlisting) {
        this.crecommendlisting = crecommendlisting == null ? null : crecommendlisting.trim();
    }

    public String getCrecommendsku() {
        return crecommendsku;
    }

    public void setCrecommendsku(String crecommendsku) {
        this.crecommendsku = crecommendsku == null ? null : crecommendsku.trim();
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
}