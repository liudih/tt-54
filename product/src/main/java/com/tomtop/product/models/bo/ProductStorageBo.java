package com.tomtop.product.models.bo;

public class ProductStorageBo extends BaseBo {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer storageId; //仓库Id
	private String storageName; //仓库名称
	
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
}
