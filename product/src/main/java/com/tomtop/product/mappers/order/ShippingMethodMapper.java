package com.tomtop.product.mappers.order;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.tomtop.product.models.dto.shipping.ShippingMethodDetail;


public interface ShippingMethodMapper {

	List<ShippingMethodDetail> getShippingMethods(
			@Param("storageId") Integer storageId,
			@Param("country") String country, @Param("weight") Double weight,
			@Param("lang") Integer lang, @Param("price") Double price,
			@Param("isSpecial") Boolean isSpecial);
	
	List<ShippingMethodDetail> getShippingMethodsByStorageIds(
			@Param("storageIds") List<Integer> storageIds,
			@Param("country") String country, @Param("weight") Double weight,
			@Param("lang") Integer lang, @Param("price") Double price,
			@Param("isSpecial") Boolean isSpecial);
	
	List<Integer> getAllShippingMethodsByStorageIds();

}
