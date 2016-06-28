package entity.loyalty;

import java.util.Date;

public class OrderCoupon {
	private Integer iid;
	private Integer istatus;
	private Integer iwebsiteid;
	private Integer iorderid;
	private String ccode;
	private String ccurrency;
	private String cemail;
	private Double fparvalue;
	private Date dusedate;
	private String cordernumber;

	

	public String getCordernumber() {
		return cordernumber;
	}

	public void setCordernumber(String cordernumber) {
		this.cordernumber = cordernumber;
	}

	public Integer getIid() {
		return iid;
	}

	public void setIid(Integer iid) {
		this.iid = iid;
	}

	public Integer getIstatus() {
		return istatus;
	}

	public void setIstatus(Integer istatus) {
		this.istatus = istatus;
	}

	public Integer getIorderid() {
		return iorderid;
	}

	public void setIorderid(Integer iorderid) {
		this.iorderid = iorderid;
	}

	public String getCcode() {
		return ccode;
	}

	public void setCcode(String ccode) {
		this.ccode = ccode;
	}

	public String getCemail() {
		return cemail;
	}

	public void setCemail(String cemail) {
		this.cemail = cemail;
	}

	public Double getFparvalue() {
		return fparvalue;
	}

	public void setFparvalue(Double fparvalue) {
		this.fparvalue = fparvalue;
	}

	public Date getDusedate() {
		return dusedate;
	}

	public void setDusedate(Date dusedate) {
		this.dusedate = dusedate;
	}

	public Integer getIwebsiteid() {
		return iwebsiteid;
	}

	public void setIwebsiteid(Integer iwebsiteid) {
		this.iwebsiteid = iwebsiteid;
	}

	public String getCcurrency() {
		return ccurrency;
	}

	public void setCcurrency(String ccurrency) {
		this.ccurrency = ccurrency;
	}

	@Override
	public String toString() {
		return "OrderCoupon [iid=" + iid + ", istatus=" + istatus
				+ ", iwebsiteid=" + iwebsiteid + ", iorderid=" + iorderid
				+ ", ccode=" + ccode + ", ccurrency=" + ccurrency + ", cemail="
				+ cemail + ", fparvalue=" + fparvalue + ", dusedate="
				+ dusedate + ", cordernumber=" + cordernumber + "]";
	}
	
}
