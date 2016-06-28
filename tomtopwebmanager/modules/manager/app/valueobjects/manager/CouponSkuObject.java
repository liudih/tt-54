package valueobjects.manager;

import java.io.Serializable;
import java.util.Date;

import entity.loyalty.CouponRule;

public class CouponSkuObject implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private Integer iid;
	private String csku;
	private Integer iruleid;
	private Boolean isEnabled;
	private String ccreateuser;
	private Date dcreatedate;
	private String cupdateuser;
	private Date dupdatedate;
	private CouponRule couponRule;
	public Integer getIid() {
		return iid;
	}
	public void setIid(Integer iid) {
		this.iid = iid;
	}
	public String getCsku() {
		return csku;
	}
	public void setCsku(String csku) {
		this.csku = csku;
	}
	public Integer getIruleid() {
		return iruleid;
	}
	public void setIruleid(Integer iruleid) {
		this.iruleid = iruleid;
	}
	
	public Boolean getIsEnabled() {
		return isEnabled;
	}
	public void setIsEnabled(Boolean isEnabled) {
		this.isEnabled = isEnabled;
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
	public String getCupdateuser() {
		return cupdateuser;
	}
	public void setCupdateuser(String cupdateuser) {
		this.cupdateuser = cupdateuser;
	}
	public Date getDupdatedate() {
		return dupdatedate;
	}
	public void setDupdatedate(Date dupdatedate) {
		this.dupdatedate = dupdatedate;
	}
	public CouponRule getCouponRule() {
		return couponRule;
	}
	public void setCouponRule(CouponRule couponRule) {
		this.couponRule = couponRule;
	}

}
