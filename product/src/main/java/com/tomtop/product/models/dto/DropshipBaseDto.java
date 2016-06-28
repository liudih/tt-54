package com.tomtop.product.models.dto;

import java.io.Serializable;

public class DropshipBaseDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2727030353244805081L;

	private String cemail;

	private String clevelname;

	private Double discount;

	private Integer iproductcount;

	public String getCemail() {
		return cemail;
	}

	public void setCemail(String cemail) {
		this.cemail = cemail;
	}

	public String getClevelname() {
		return clevelname;
	}

	public void setClevelname(String clevelname) {
		this.clevelname = clevelname;
	}

	public Double getDiscount() {
		return discount;
	}

	public void setDiscount(Double discount) {
		this.discount = discount;
	}

	public Integer getIproductcount() {
		return iproductcount;
	}

	public void setIproductcount(Integer iproductcount) {
		this.iproductcount = iproductcount;
	}

}
