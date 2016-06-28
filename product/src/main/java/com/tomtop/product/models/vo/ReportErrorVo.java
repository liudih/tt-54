package com.tomtop.product.models.vo;

import java.io.Serializable;

public class ReportErrorVo implements Serializable {

	
    /**
	 * 
	 */
	private static final long serialVersionUID = 4832337848447890621L;
	private String listingId;
    private String sku;
    private String errorType;
    private String email;
    private String writeInquiry;
    

	public String getListingId() {
		return listingId;
	}
	public void setListingId(String listingId) {
		this.listingId = listingId;
	}
	public String getSku() {
		return sku;
	}
	public void setSku(String sku) {
		this.sku = sku;
	}
	public String getErrorType() {
		return errorType;
	}
	public void setErrorType(String errorType) {
		this.errorType = errorType;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getWriteInquiry() {
		return writeInquiry;
	}
	public void setWriteInquiry(String writeInquiry) {
		this.writeInquiry = writeInquiry;
	}
}
