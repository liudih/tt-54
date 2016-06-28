package com.tomtop.product.models.bo;

import java.util.List;

public class ProductStorageShippingBo extends BaseBo {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer storageId; //仓库Id
	private String storageName; //仓库名称
	private List<ShippingMethodBo> shippingMethods;//仓库对应的邮寄方式
	
	public Integer getStorageId() {
		return storageId;
	}
	public void setStorageId(Integer storageId) {
		this.storageId = storageId;
	}
	public String getStorageName() {
		return storageName;
	}
	public void setStorageName(String storageName) {
		this.storageName = storageName;
	}
	public List<ShippingMethodBo> getShippingMethods() {
		return shippingMethods;
	}
	public void setShippingMethods(List<ShippingMethodBo> shippingMethods) {
		this.shippingMethods = shippingMethods;
	}
}
