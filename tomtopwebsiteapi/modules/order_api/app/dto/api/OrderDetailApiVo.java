package dto.api;

import java.io.Serializable;
import java.util.Date;

public class OrderDetailApiVo implements Serializable {
	private Integer iorderid;
	private String clistingid;
	private Integer iqty;
	private Double fprice;
	private Double ftotalprices;
	private String csku;
	private Date dcreatedate;
	public Integer getIorderid() {
		return iorderid;
	}
	public void setIorderid(Integer iorderid) {
		this.iorderid = iorderid;
	}
	public String getClistingid() {
		return clistingid;
	}
	public void setClistingid(String clistingid) {
		this.clistingid = clistingid;
	}
	public Integer getIqty() {
		return iqty;
	}
	public void setIqty(Integer iqty) {
		this.iqty = iqty;
	}
	public Double getFprice() {
		return fprice;
	}
	public void setFprice(Double fprice) {
		this.fprice = fprice;
	}
	public Double getFtotalprices() {
		return ftotalprices;
	}
	public void setFtotalprices(Double ftotalprices) {
		this.ftotalprices = ftotalprices;
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
