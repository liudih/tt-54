package com.tomtop.product.models.dto.base;

/**
 * 
 * @ClassName: SkuRelateListingId
 * @Description: TODO(sku与listingId的映射)
 * @author yinfei
 * @date 2015年11月2日
 *
 */
public class SkuRelateListingId {
	private String csku;
	private String clistingid;

	public String getCsku() {
		return csku;
	}

	public void setCsku(String csku) {
		this.csku = csku;
	}

	public String getClistingid() {
		return clistingid;
	}

	public void setClistingid(String clistingid) {
		this.clistingid = clistingid;
	}

}
