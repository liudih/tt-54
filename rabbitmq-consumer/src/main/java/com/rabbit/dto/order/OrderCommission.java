package com.rabbit.dto.order;

import java.io.Serializable;
import java.util.Date;


public class OrderCommission implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7077321649472466170L;
	private Integer iid;
	private double fordersubtotal;
	private double fextra;
	private Integer istatus;
	private String ccurrency;
	private String corigin;
	private Integer iwebsiteid;
	private String cordernumber;
	private String csku;
	private Date dcreatedate;
	
	public Integer getIid() {
		return iid;
	}
	public void setIid(Integer iid) {
		this.iid = iid;
	}
	public double getFordersubtotal() {
		return fordersubtotal;
	}
	public void setFordersubtotal(double fordersubtotal) {
		this.fordersubtotal = fordersubtotal;
	}
	public double getFextra() {
		return fextra;
	}
	public void setFextra(double fextra) {
		this.fextra = fextra;
	}
	public Integer getIstatus() {
		return istatus;
	}
	public void setIstatus(Integer istatus) {
		this.istatus = istatus;
	}
	public String getCcurrency() {
		return ccurrency;
	}
	public void setCcurrency(String ccurrency) {
		this.ccurrency = ccurrency;
	}
	public String getCorigin() {
		return corigin;
	}
	public void setCorigin(String corigin) {
		this.corigin = corigin;
	}
	public Integer getIwebsiteid() {
		return iwebsiteid;
	}
	public void setIwebsiteid(Integer iwebsiteid) {
		this.iwebsiteid = iwebsiteid;
	}
	public String getCordernumber() {
		return cordernumber;
	}
	public void setCordernumber(String cordernumber) {
		this.cordernumber = cordernumber;
	}
	public String getCsku() {
		return csku;
	}
	public void setCsku(String csku) {
		this.csku = csku;
	}
	public Date getDcreatedate() {
		return dcreatedate;
	}
	public void setDcreatedate(Date dcreatedate) {
		this.dcreatedate = dcreatedate;
	}
}
