package com.website.dto.member;

public class MemberPoint {

	private Integer id;
	private Integer websiteid;
	private String email;
	private String dotype;
	private Double integral;
	private String remark;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getWebsiteid() {
		return websiteid;
	}

	public void setWebsiteid(Integer websiteid) {
		this.websiteid = websiteid;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getDotype() {
		return dotype;
	}

	public void setDotype(String dotype) {
		this.dotype = dotype;
	}

	public Double getIntegral() {
		return integral;
	}

	public void setIntegral(Double integral) {
		this.integral = integral;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

}
