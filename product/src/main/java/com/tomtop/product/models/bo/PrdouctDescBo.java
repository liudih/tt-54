package com.tomtop.product.models.bo;

public class PrdouctDescBo extends BaseBo {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7973748345208057374L;
	private String desc;
	private String shippingPayment = null;
	private String warranty = null;

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getShippingPayment() {
		return shippingPayment;
	}

	public void setShippingPayment(String shippingPayment) {
		this.shippingPayment = shippingPayment;
	}

	public String getWarranty() {
		return warranty;
	}

	public void setWarranty(String warranty) {
		this.warranty = warranty;
	}
}
