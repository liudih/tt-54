package entity.loyalty;

import java.io.Serializable;
import java.util.Date;

public class IntegralUseRule  implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7905327406556513844L;
	private Integer iid;
	private Integer iwebsiteid;
	private Integer imembergroupid;
	private Integer iintegral;
	private Double fmoney;
	private String ccurrency;
	private Integer imaxuse;
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

	public Integer getImembergroupid() {
		return imembergroupid;
	}

	public void setImembergroupid(Integer imembergroupid) {
		this.imembergroupid = imembergroupid;
	}

	public Integer getIintegral() {
		return iintegral;
	}

	public void setIintegral(Integer iintegral) {
		this.iintegral = iintegral;
	}

	public Double getFmoney() {
		return fmoney;
	}

	public void setFmoney(Double fmoney) {
		this.fmoney = fmoney;
	}

	public String getCcurrency() {
		return ccurrency;
	}

	public void setCcurrency(String ccurrency) {
		this.ccurrency = ccurrency;
	}

	public Integer getImaxuse() {
		return imaxuse;
	}

	public void setImaxuse(Integer imaxuse) {
		this.imaxuse = imaxuse;
	}

	public Date getDcreatedate() {
		return dcreatedate;
	}

	public void setDcreatedate(Date dcreatedate) {
		this.dcreatedate = dcreatedate;
	}
}
