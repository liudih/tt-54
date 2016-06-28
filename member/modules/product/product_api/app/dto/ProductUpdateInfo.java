package dto;

import java.io.Serializable;

public class ProductUpdateInfo implements Serializable{

	private static final long serialVersionUID = 1L;
	/**
	 * productType:TODO（要修改成哪类产品标签，ProductLabelType）
	 */
	private String productType;
	private Integer websiteId;
	private String storageName;
	private String status;
	private Integer quantity;
	private String listingId;
	private String sku;

	public ProductUpdateInfo(String productType, Integer websiteId,
			String storageName, String status, Integer quantity,
			String listingId, String sku) {
		super();
		this.productType = productType;
		this.websiteId = websiteId;
		this.storageName = storageName;
		this.status = status;
		this.quantity = quantity;
		this.listingId = listingId;
		this.sku = sku;
	}

	public String getProductType() {
		return productType;
	}

	public Integer getWebsiteId() {
		return websiteId;
	}

	public String getStatus() {
		return status;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public String getListingId() {
		return listingId;
	}

	public String getSku() {
		return sku;
	}

	public void setProductType(String productType) {
		this.productType = productType;
	}

	public void setWebsiteId(Integer websiteId) {
		this.websiteId = websiteId;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public void setListingId(String listingId) {
		this.listingId = listingId;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	public String getStorageName() {
		return storageName;
	}

	public void setStorageName(String storageName) {
		this.storageName = storageName;
	}

}
