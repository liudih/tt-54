package dto.order.dropShipping;

import java.io.Serializable;
import java.util.Date;

public class DropShippingOrderDetail implements Serializable {
	private Integer iid;
	private Integer iorderid;
	private String csku;
	private String clistingid;
	private Integer iqty;
	private Double fprice;
	private Double ftotalprice;
	private Date dcreateDate;
	private String ctitle;
	private Double foriginalprice;

	public Integer getIid() {
		return iid;
	}

	public void setIid(Integer iid) {
		this.iid = iid;
	}

	public Integer getIorderid() {
		return iorderid;
	}

	public void setIorderid(Integer iorderid) {
		this.iorderid = iorderid;
	}

	public String getCsku() {
		return csku;
	}

	public void setCsku(String csku) {
		this.csku = csku;
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

	public Double getFtotalprice() {
		return ftotalprice;
	}

	public void setFtotalprice(Double ftotalprice) {
		this.ftotalprice = ftotalprice;
	}

	public Date getDcreateDate() {
		return dcreateDate;
	}

	public void setDcreateDate(Date dcreateDate) {
		this.dcreateDate = dcreateDate;
	}

	public String getCtitle() {
		return ctitle;
	}

	public void setCtitle(String ctitle) {
		this.ctitle = ctitle;
	}

	public Double getForiginalprice() {
		return foriginalprice;
	}

	public void setForiginalprice(Double foriginalprice) {
		this.foriginalprice = foriginalprice;
	}

}
