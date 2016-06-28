package com.tomtop.product.services.dropship;

import com.tomtop.product.models.bo.BaseBo;

public interface IDropshipProductService {

	public BaseBo addProductDropshipBySku(String email,String sku,Integer siteId);
	public BaseBo addProductDropshipByListingId(String email,String listingId,Integer siteId);
}
