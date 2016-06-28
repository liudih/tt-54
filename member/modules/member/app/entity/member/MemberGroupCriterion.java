package entity.member;

import java.sql.Date;

public class MemberGroupCriterion {

	private Integer iid; 
	
	private Integer iwebsiteid;
	
	private Integer igroupid;
	
	private Double dconsumptionprice;
	
	private String ccreateuser;
	
	private Date dcreatedate;

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

	public Integer getIgroupid() {
		return igroupid;
	}

	public void setIgroupid(Integer igroupid) {
		this.igroupid = igroupid;
	}

	public Double getDconsumptionprice() {
		return dconsumptionprice;
	}

	public void setDconsumptionprice(Double dconsumptionprice) {
		this.dconsumptionprice = dconsumptionprice;
	}

	public String getCcreateuser() {
		return ccreateuser;
	}

	public void setCcreateuser(String ccreateuser) {
		this.ccreateuser = ccreateuser;
	}

	public Date getDcreatedate() {
		return dcreatedate;
	}

	public void setDcreatedate(Date dcreatedate) {
		this.dcreatedate = dcreatedate;
	}
}
