package com.tomtop.product.models.bo;


import java.io.Serializable;

import com.tomtop.product.models.dto.shipping.ShippingMethodDetail;

public class ShippingMethodBo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer id;
	private String name;
	private String title;
	private String context;
	private String imgUrl;
	private Double freight;
	private String code;
	private Integer groupId;
	private Integer storageId;
	private boolean isSpecial;
	private boolean isFree;
	
	public ShippingMethodBo(){
		
	}
	public ShippingMethodBo(ShippingMethodDetail method,
			String context, Double freight) {
		this.id = method.getIid();
		this.name = method.getCname();
		this.imgUrl = method.getCimageurl();
		this.context = context;
		this.freight = freight;
		this.title = method.getCtitle();
		this.code = method.getCcode();
		this.groupId = method.getIgroupid();
		this.storageId = method.getIstorageid();
		this.isSpecial = method.getBisspecial();
		this.isFree = method.getBexistfree();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getContext() {
		return context;
	}

	public void setContext(String context) {
		this.context = context;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public Double getFreight() {
		return freight;
	}

	public void setFreight(Double freight) {
		this.freight = freight;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Integer getGroupId() {
		return groupId;
	}

	public void setGroupId(Integer groupId) {
		this.groupId = groupId;
	}

	public boolean isSpecial() {
		return isSpecial;
	}

	public void setSpecial(boolean isSpecial) {
		this.isSpecial = isSpecial;
	}

	public boolean isFree() {
		return isFree;
	}

	public void setFree(boolean isFree) {
		this.isFree = isFree;
	}
	public Integer getStorageId() {
		return storageId;
	}
	public void setStorageId(Integer storageId) {
		this.storageId = storageId;
	}
	@Override
	public String toString() {
		return "ShippingMethodBo [id=" + id + ", name=" + name
				+ ", title=" + title + ", context=" + context + ", imgUrl="
				+ imgUrl + ", freight=" + freight + ", code=" + code
				+ ", groupId=" + groupId + ", isSpecial=" + isSpecial
				+ ", isFree=" + isFree + "]";
	}
}
