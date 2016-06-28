package forms;

import play.data.validation.Constraints.Required;

public class WholeSaleDiscountForm {
	private Integer iid;
	@Required
	private Integer iwebsiteid;
	@Required
	private Double fstartprice;
	@Required
	private Double fendprice;
	@Required
	private Double fmindiscount;
	@Required
	private Double fmaxdiscount;

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

	public Double getFstartprice() {
		return fstartprice;
	}

	public void setFstartprice(Double fstartprice) {
		this.fstartprice = fstartprice;
	}

	public Double getFendprice() {
		return fendprice;
	}

	public void setFendprice(Double fendprice) {
		this.fendprice = fendprice;
	}

	public Double getFmindiscount() {
		return fmindiscount;
	}

	public void setFmindiscount(Double fmindiscount) {
		this.fmindiscount = fmindiscount;
	}

	public Double getFmaxdiscount() {
		return fmaxdiscount;
	}

	public void setFmaxdiscount(Double fmaxdiscount) {
		this.fmaxdiscount = fmaxdiscount;
	}

}
