package dto.order;

import java.util.Date;

public class PreparatoryDetail {
	private Integer iid;
	private String cid;
	private Integer iorderid;
	private String ctitle;
	private String csku;
	private String clistingid;
	private Integer iqty;
	private Double fprice;
	private Double ftotalprices;
	private Date dcreatedate;
	private Double fweight;
	private String cparentid;
	private Double foriginalprice;
	private Boolean bismain;

	public Integer getIid() {
		return iid;
	}

	public void setIid(Integer iid) {
		this.iid = iid;
	}

	public String getCid() {
		return cid;
	}

	public void setCid(String cid) {
		this.cid = cid;
	}

	public Integer getIorderid() {
		return iorderid;
	}

	public void setIorderid(Integer iorderid) {
		this.iorderid = iorderid;
	}

	public String getCtitle() {
		return ctitle;
	}

	public void setCtitle(String ctitle) {
		this.ctitle = ctitle;
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

	public Double getFtotalprices() {
		return ftotalprices;
	}

	public void setFtotalprices(Double ftotalprices) {
		this.ftotalprices = ftotalprices;
	}

	public Date getDcreatedate() {
		return dcreatedate;
	}

	public void setDcreatedate(Date dcreatedate) {
		this.dcreatedate = dcreatedate;
	}

	public Double getFweight() {
		return fweight;
	}

	public void setFweight(Double fweight) {
		this.fweight = fweight;
	}

	public String getCparentid() {
		return cparentid;
	}

	public void setCparentid(String cparentid) {
		this.cparentid = cparentid;
	}

	public Double getForiginalprice() {
		return foriginalprice;
	}

	public void setForiginalprice(Double foriginalprice) {
		this.foriginalprice = foriginalprice;
	}

	public Boolean getBismain() {
		return bismain;
	}

	public void setBismain(Boolean bismain) {
		this.bismain = bismain;
	}

}
