package dto.order.dropShipping;

import java.io.Serializable;
import java.util.Date;

public class DropShipping implements Serializable {
	private Integer iid;
	private String cdropshippingid;
	private String cuseremail;
	private Double ftotalprice;
	private Boolean bpaid;
	private String ccurrency;
	private Date dcreatedate;
	private Integer iwebsiteid;
	private Boolean bused;

	public Integer getIid() {
		return iid;
	}

	public void setIid(Integer iid) {
		this.iid = iid;
	}

	public String getCdropshippingid() {
		return cdropshippingid;
	}

	public void setCdropshippingid(String cdropshippingid) {
		this.cdropshippingid = cdropshippingid;
	}

	public String getCuseremail() {
		return cuseremail;
	}

	public void setCuseremail(String cuseremail) {
		this.cuseremail = cuseremail;
	}

	public Double getFtotalprice() {
		return ftotalprice;
	}

	public void setFtotalprice(Double ftotalprice) {
		this.ftotalprice = ftotalprice;
	}

	public Boolean getBpaid() {
		return bpaid;
	}

	public void setBpaid(Boolean bpaid) {
		this.bpaid = bpaid;
	}

	public String getCcurrency() {
		return ccurrency;
	}

	public void setCcurrency(String ccurrency) {
		this.ccurrency = ccurrency;
	}

	public Date getDcreatedate() {
		return dcreatedate;
	}

	public void setDcreatedate(Date dcreatedate) {
		this.dcreatedate = dcreatedate;
	}

	public Integer getIwebsiteid() {
		return iwebsiteid;
	}

	public void setIwebsiteid(Integer iwebsiteid) {
		this.iwebsiteid = iwebsiteid;
	}

	public Boolean getBused() {
		return bused;
	}

	public void setBused(Boolean bused) {
		this.bused = bused;
	}

}
