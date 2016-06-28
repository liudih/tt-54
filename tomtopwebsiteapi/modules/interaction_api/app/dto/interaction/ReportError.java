package dto.interaction;

import java.io.Serializable;
import java.util.Date;

public class ReportError implements Serializable  {
	private static final long serialVersionUID = 1L;
    private Integer iid;

    private String clistingid;

    private String csku;

    private String cerrortype;

    private String cemail;

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

    public String getCerrortype() {
        return cerrortype;
    }

    public void setCerrortype(String cerrortype) {
        this.cerrortype = cerrortype == null ? null : cerrortype.trim();
    }

    public String getCemail() {
        return cemail;
    }

    public void setCemail(String cemail) {
        this.cemail = cemail == null ? null : cemail.trim();
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