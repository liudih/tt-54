package com.tomtop.product.services.storage;

import java.util.List;

import com.tomtop.product.models.bo.CategoryStorageBo;
import com.tomtop.product.models.bo.ProductStorageShippingBo;
import com.tomtop.product.models.bo.ShippingMethodBo;

public interface IProductStorageService {

	public List<ProductStorageShippingBo> getProductStorage(String listingId,Integer qty, Integer langId,
			Integer siteId, String currency,String country);
	
	public List<CategoryStorageBo> getAllCategoryStorageBo();
	
	public List<ShippingMethodBo> getStoragesShippingMethod(Integer storageId,String listingId,Integer qty, Integer langId,
			Integer siteId, String currency,String country);
}
