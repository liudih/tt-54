package entity.attribute;

public class AttributeValueName {
    private Integer ikeyid;

    private Integer ilanguageid;

    private Integer ivalueid;

    private String cvaluename;

    public Integer getIkeyid() {
        return ikeyid;
    }

    public void setIkeyid(Integer ikeyid) {
        this.ikeyid = ikeyid;
    }

    public Integer getIlanguageid() {
        return ilanguageid;
    }

    public void setIlanguageid(Integer ilanguageid) {
        this.ilanguageid = ilanguageid;
    }

    public Integer getIvalueid() {
        return ivalueid;
    }

    public void setIvalueid(Integer ivalueid) {
        this.ivalueid = ivalueid;
    }

    public String getCvaluename() {
        return cvaluename;
    }

    public void setCvaluename(String cvaluename) {
        this.cvaluename = cvaluename == null ? null : cvaluename.trim();
    }
}