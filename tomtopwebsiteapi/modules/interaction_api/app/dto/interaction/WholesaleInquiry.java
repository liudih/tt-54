package dto.interaction;

import java.io.Serializable;
import java.util.Date;

public class WholesaleInquiry implements Serializable  {
	private static final long serialVersionUID = 1L;
    private Integer iid;

    private String clistingid;

    private String csku;

    private String cname;

    private String cphone;

    private String cemail;

    private Double ftargetprice;

    private Integer iquantity;

    private String ccountrystate;

    private String ccompany;

    private String cinquiry;

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

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname == null ? null : cname.trim();
    }

    public String getCphone() {
        return cphone;
    }

    public void setCphone(String cphone) {
        this.cphone = cphone == null ? null : cphone.trim();
    }

    public String getCemail() {
        return cemail;
    }

    public void setCemail(String cemail) {
        this.cemail = cemail == null ? null : cemail.trim();
    }

    public Double getFtargetprice() {
        return ftargetprice;
    }

    public void setFtargetprice(Double ftargetprice) {
        this.ftargetprice = ftargetprice;
    }

    public Integer getIquantity() {
        return iquantity;
    }

    public void setIquantity(Integer iquantity) {
        this.iquantity = iquantity;
    }

    public String getCcountrystate() {
        return ccountrystate;
    }

    public void setCcountrystate(String ccountrystate) {
        this.ccountrystate = ccountrystate == null ? null : ccountrystate.trim();
    }

    public String getCcompany() {
        return ccompany;
    }

    public void setCcompany(String ccompany) {
        this.ccompany = ccompany == null ? null : ccompany.trim();
    }

    public String getCinquiry() {
        return cinquiry;
    }

    public void setCinquiry(String cinquiry) {
        this.cinquiry = cinquiry == null ? null : cinquiry.trim();
    }

    public Date getDcreatedate() {
        return dcreatedate;
    }

    public void setDcreatedate(Date dcreatedate) {
        this.dcreatedate = dcreatedate;
    }
}