package entity.loyalty;

import java.util.Date;

/**
 * coupon sku关系对应表
 * 
 * @author liuxin
 *
 */
public class CouponSku {
	private Integer iid;
	private String csku;
	private Integer iruleid;
	private Boolean isEnabled;
	private String ccreateuser;
	private Date dcreatedate;
	private String cupdateuser;
	private Date dupdatedate;

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
}
