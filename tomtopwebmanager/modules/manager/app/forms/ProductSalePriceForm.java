package forms;

import java.util.Date;

public class ProductSalePriceForm {
    private Integer iid;

    private String clistingid;

    private String csku;

    private Double fsaleprice;

    private String dbegindate;

    private String denddate;

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

    public String getDbegindate() {
		return dbegindate;
	}

	public void setDbegindate(String dbegindate) {
		this.dbegindate = dbegindate;
	}

	public String getDenddate() {
		return denddate;
	}

	public void setDenddate(String denddate) {
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
}