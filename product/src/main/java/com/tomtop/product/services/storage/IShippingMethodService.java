package com.tomtop.product.services.storage;

import java.util.List;
import java.util.Map;

import com.tomtop.product.models.bo.ShippingMethodBo;
import com.tomtop.product.models.dto.shipping.ShippingMethodDetail;
import com.tomtop.product.models.dto.shipping.ShippingMethodDto;
import com.tomtop.search.entiry.IndexEntity;

public interface IShippingMethodService {

	public ShippingMethodDto getShippingMethodDto(Integer storageID,
			String listingID, Integer qty,String country,Integer langId,
			String currency,Integer siteId);
	
	public boolean hasFreeShipping(String listingId);
	
	public boolean hasAllFreeShipping(List<String> listingId);
	
	public boolean isSpecial(List<String> listingIds);
	
	public List<ShippingMethodBo> getShippingMethodInformations(
			ShippingMethodDto requst);
			
	public List<ShippingMethodDetail> getShippingMethods(Integer storageId,
			String country, Double weight, Integer lang, Double subTotal,
			Boolean isSpecial);
	
	public ShippingMethodDto getStorageShippingMethodDto(Integer storageIDs,
			String listingID, Integer qty, String country, Integer langId,
			String currency, Integer siteId,IndexEntity index);
	
	public List<ShippingMethodBo> getStorageShippingMethodInformations(
			ShippingMethodDto requst);
	
	public Map<Integer, Integer> getShippingMethodByStorageId();
}
