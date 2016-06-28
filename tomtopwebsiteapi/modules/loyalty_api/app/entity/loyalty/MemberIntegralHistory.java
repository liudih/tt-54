package entity.loyalty;

import java.io.Serializable;
import java.util.Date;

public class MemberIntegralHistory implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Integer iid;
	private Integer iwebsiteid;
	private String cemail;
	private String cdotype;
	private Double iintegral;
	private String cremark;
	private Integer istatus;
	private Date dcreatedate;
	private String csource;
	//add by lijun
	private String createDateStr;

	
	public String getCreateDateStr() {
		return createDateStr;
	}

	public void setCreateDateStr(String createDateStr) {
		this.createDateStr = createDateStr;
	}

	public Integer getIid() {
		return iid;
	}

	public void setIid(Integer iid) {
		this.iid = iid;
	}

	public Integer getIwebsiteid() {
		return iwebsiteid;
	}

	public void setIwebsiteid(Integer iwebsiteid) {
		this.iwebsiteid = iwebsiteid;
	}

	public String getCemail() {
		return cemail;
	}

	public void setCemail(String cemail) {
		this.cemail = cemail;
	}

	public String getCdotype() {
		return cdotype;
	}

	public void setCdotype(String cdotype) {
		this.cdotype = cdotype;
	}

	public Double getIintegral() {
		return iintegral;
	}

	public void setIintegral(Double iintegral) {
		this.iintegral = iintegral;
	}

	public String getCremark() {
		return cremark;
	}

	public void setCremark(String cremark) {
		this.cremark = cremark;
	}

	public Integer getIstatus() {
		return istatus;
	}

	public void setIstatus(Integer istatus) {
		this.istatus = istatus;
	}

	public Date getDcreatedate() {
		return dcreatedate;
	}

	public void setDcreatedate(Date dcreatedate) {
		this.dcreatedate = dcreatedate;
	}

	public String getCsource() {
		return csource;
	}

	public void setCsource(String csource) {
		this.csource = csource;
	}
	
	
}
