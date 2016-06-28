package entity.loyalty;

import java.util.Date;

public class OrderPoints {
	private Integer iid;
	private Integer ipointsid;
	private Double fparvalue;
	private Integer istatus;
	private Integer iorderid;
	private String cemail;
	private Date dusedate;

	public Integer getIid() {
		return iid;
	}

	public void setIid(Integer iid) {
		this.iid = iid;
	}

	public Integer getIpointsid() {
		return ipointsid;
	}

	public void setIpointsid(Integer ipointsid) {
		this.ipointsid = ipointsid;
	}

	public Double getFparvalue() {
		return fparvalue;
	}

	public void setFparvalue(Double fparvalue) {
		this.fparvalue = fparvalue;
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

	public String getCemail() {
		return cemail;
	}

	public void setCemail(String cemail) {
		this.cemail = cemail;
	}

	public Date getDusedate() {
		return dusedate;
	}

	public void setDusedate(Date dusedate) {
		this.dusedate = dusedate;
	}

}
