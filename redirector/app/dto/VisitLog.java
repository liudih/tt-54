package dto;

import java.util.Date;

public class VisitLog {
    private String curl;

    private String caid;
    
    private String ctaskid;
    
    private Integer itasktype;

    private String csource;

    private String cip;

    private Date dcreatedate;

	public String getCurl() {
		return curl;
	}

	public String getCaid() {
		return caid;
	}

	public String getCsource() {
		if(csource==null) csource = "";
		return csource;
	}

	public String getCip() {
		return cip;
	}

	public Date getDcreatedate() {
		return dcreatedate;
	}

	public String getCtaskid() {
		if(ctaskid==null) ctaskid = "";
		return ctaskid;
	}

	public Integer getItasktype() {
		return itasktype;
	}

}