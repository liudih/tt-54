package com.tomtop.product.models.dto.base;

import java.io.Serializable;
import java.util.Date;

public class ProductBundleSale implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer iid;

	private String clistingid;

	private String cbundlelistingid;

	private Double fdiscount;

	private Integer ipriority;

	private String clastupdateuser;

	private Date dlastupdatedate;

	private String ccreateuser;

	private Date dcreatedate;

	private boolean bvisible;

	private boolean bavailable;

	private boolean bactivity;

	public Integer getIid() {
		return iid;
	}

	public void setIid(Integer iid) {
		this.iid = iid;
	}

	public String getClistingid() {
		return clistingid;
	}

	public void setClistingid(String clistingid) {
		this.clistingid = clistingid == null ? null : clistingid.trim();
	}

	public String getCbundlelistingid() {
		return cbundlelistingid;
	}

	public void setCbundlelistingid(String cbundellistingid) {
		this.cbundlelistingid = cbundellistingid == null ? null
				: cbundellistingid.trim();
	}

	public Double getFdiscount() {
		return fdiscount;
	}

	public void setFdiscount(Double fdiscount) {
		this.fdiscount = fdiscount;
	}

	public Integer getIpriority() {
		return ipriority;
	}

	public void setIpriority(Integer ipriority) {
		this.ipriority = ipriority;
	}

	public String getClastupdateuser() {
		return clastupdateuser;
	}

	public void setClastupdateuser(String clastupdateuser) {
		this.clastupdateuser = clastupdateuser == null ? null : clastupdateuser
				.trim();
	}

	public Date getDlastupdatedate() {
		return dlastupdatedate;
	}

	public void setDlastupdatedate(Date dlastupdatedate) {
		this.dlastupdatedate = dlastupdatedate;
	}

	public String getCcreateuser() {
		return ccreateuser;
	}

	public void setCcreateuser(String ccreateuser) {
		this.ccreateuser = ccreateuser == null ? null : ccreateuser.trim();
	}

	public Date getDcreatedate() {
		return dcreatedate;
	}

	public void setDcreatedate(Date dcreatedate) {
		this.dcreatedate = dcreatedate;
	}

	public Boolean getBvisible() {
		return bvisible;
	}

	public void setBvisible(Boolean bvisible) {
		this.bvisible = bvisible;
	}

	public Boolean getBavailable() {
		return bavailable;
	}

	public void setBavailable(Boolean bavailable) {
		this.bavailable = bavailable;
	}

	public boolean isBactivity() {
		return bactivity;
	}

	public void setBactivity(boolean bactivity) {
		this.bactivity = bactivity;
	}

}