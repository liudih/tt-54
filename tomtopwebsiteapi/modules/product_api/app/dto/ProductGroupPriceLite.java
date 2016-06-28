package dto;

import java.io.Serializable;

import services.base.utils.DoubleCalculateUtils;

public class ProductGroupPriceLite implements Serializable {
	private static final long serialVersionUID = 1L;

	private Integer icustomergroupid;

	private Integer iqty;

	private Double fprice;

	private Double fdiscount;

	private Double fexchangerate;

	public Double getFexchangerate() {
		return fexchangerate;
	}

	public void setFexchangerate(Double fexchangerate) {
		this.fexchangerate = fexchangerate;
	}

	public Double getFdiscount() {
		return fdiscount;
	}

	public void setFdiscount(Double fdiscount) {
		this.fdiscount = fdiscount;
	}

	public Integer getIcustomergroupid() {
		return icustomergroupid;
	}

	public void setIcustomergroupid(Integer icustomergroupid) {
		this.icustomergroupid = icustomergroupid;
	}

	public Integer getIqty() {
		return iqty;
	}

	public void setIqty(Integer iqty) {
		this.iqty = iqty;
	}

	public Double getFprice() {
		DoubleCalculateUtils cu = new DoubleCalculateUtils(fprice);
		DoubleCalculateUtils cdisocunt = new DoubleCalculateUtils(1);
		return cu
				.multiply(cdisocunt.subtract(this.getFdiscount()).doubleValue())
				.multiply(this.fexchangerate).doubleValue();
	}

	public void setFprice(Double fprice) {
		this.fprice = fprice;
	}
}
