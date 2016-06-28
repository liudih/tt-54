package events.base;


public class VisitEvent {
	final String curl;

    final String cshorturlcode;

    final String caid;
    
    final Integer itasktype;

    final String csource;

    final String cip;
    
    final String ctaskid;
    

	public VisitEvent(String curl, String cshorturlcode, String caid,
			String csource, String cip,String ctaskid,Integer itasktype) {
		super();
		this.curl = curl;
		this.cshorturlcode = cshorturlcode;
		this.caid = caid;
		this.csource = csource;
		this.cip = cip;
		this.ctaskid = ctaskid;
		this.itasktype = itasktype;
	}
	public String getCurl() {
		return curl;
	}

	public String getCshorturlcode() {
		return cshorturlcode;
	}

	public String getCaid() {
		return caid;
	}

	public String getCsource() {
		return csource;
	}

	public String getCip() {
		return cip;
	}
	public String getCtaskid() {
		return ctaskid;
	}
	public Integer getItasktype() {
		return itasktype;
	}

}
