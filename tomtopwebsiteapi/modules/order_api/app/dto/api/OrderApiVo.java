package dto.api;

import java.io.Serializable;
import java.util.Date;

public class OrderApiVo implements Serializable {
	private Integer iid;
	private Double fshippingprice;
	private double fordersubtotal;
	private double fextra;
	private double fgrandtotal;
	private Integer istatus;
	private String ccurrency;
	private Date dcreatedate;
	private String corigin;
	private String cip;
	private String cordernumber;
	private String ctransactionid;
	private Double rate;
	public Integer getIid() {
		return iid;
	}
	public void setIid(Integer iid) {
		this.iid = iid;
	}
	public Double getFshippingprice() {
		return fshippingprice;
	}
	public void setFshippingprice(Double fshippingprice) {
		this.fshippingprice = fshippingprice;
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
	public double getFgrandtotal() {
		return fgrandtotal;
	}
	public void setFgrandtotal(double fgrandtotal) {
		this.fgrandtotal = fgrandtotal;
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
	public String getCorigin() {
		return corigin;
	}
	public void setCorigin(String corigin) {
		this.corigin = corigin;
	}
	public String getCip() {
		return cip;
	}
	public void setCip(String cip) {
		this.cip = cip;
	}
	public String getCordernumber() {
		return cordernumber;
	}
	public void setCordernumber(String cordernumber) {
		this.cordernumber = cordernumber;
	}
	public String getCtransactionid() {
		return ctransactionid;
	}
	public void setCtransactionid(String ctransactionid) {
		this.ctransactionid = ctransactionid;
	}
	public Double getRate() {
		return rate;
	}
	public void setRate(Double rate) {
		this.rate = rate;
	}
	public String getCcurrency() {
		return ccurrency;
	}
	public void setCcurrency(String ccurrency) {
		this.ccurrency = ccurrency;
	}
}
