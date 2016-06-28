package entity.base;

import java.util.Date;

public class VisitLog {
    private String curl;

    private String cshorturlcode;

    private String caid;
    
    private String ctaskid;
    
    private Integer itasktype;

    private String csource;

    private String cip;

    private Date dcreatedate;

    public String getCurl() {
        return curl;
    }

    public void setCurl(String curl) {
        this.curl = curl == null ? "" : curl.trim();
    }

    public String getCshorturlcode() {
        return cshorturlcode;
    }

    public void setCshorturlcode(String cshorturlcode) {
        this.cshorturlcode = cshorturlcode == null ? "" : cshorturlcode.trim();
    }

    public String getCaid() {
        return caid;
    }

    public void setCaid(String caid) {
        this.caid = caid == null ? "" : caid.trim();
    }

    public String getCsource() {
        return csource;
    }

    public void setCsource(String csource) {
        this.csource = csource == null ? "" : csource.trim();
    }

    public String getCip() {
        return cip;
    }

    public void setCip(String cip) {
        this.cip = cip == null ? "" : cip.trim();
    }

    public Date getDcreatedate() {
        return dcreatedate;
    }

    public void setDcreatedate(Date dcreatedate) {
        this.dcreatedate = dcreatedate;
    }

	public String getCtaskid() {
		return ctaskid;
	}

	public void setCtaskid(String ctaskid) {
		this.ctaskid = ctaskid == null ? "" : ctaskid.trim();
	}

	public Integer getItasktype() {
		return itasktype;
	}

	public void setItasktype(Integer itasktype) {
		this.itasktype = itasktype;
	}
}