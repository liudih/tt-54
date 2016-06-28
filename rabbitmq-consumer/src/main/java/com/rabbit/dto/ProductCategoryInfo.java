package com.rabbit.dto;

/**
 * 
 * @ClassName: ProductCategoryInfo
 * @Description: TODO(产品类目信息（用于订单导出）)
 * @author yinfei
 * @date 2015年9月14日
 *
 */
public class ProductCategoryInfo {
	private Integer icategory;
	private String clistingid;
	private String cname;
	private String cpath;
	
	public Integer getIcategory() {
		return icategory;
	}
	public void setIcategory(Integer icategory) {
		this.icategory = icategory;
	}
	public String getClistingid() {
		return clistingid;
	}
	public void setClistingid(String clistingid) {
		this.clistingid = clistingid;
	}
	public String getCname() {
		return cname;
	}
	public void setCname(String cname) {
		this.cname = cname;
	}
	public String getCpath() {
		return cpath;
	}
	public void setCpath(String cpath) {
		this.cpath = cpath;
	}

}
