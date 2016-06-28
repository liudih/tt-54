package com.tomtop.product.services;

import java.util.List;

import com.tomtop.product.models.bo.ProductHotBo;

public interface IProductHotService {

	public List<String> getProductTypeList(int siteId,String type, int page,
			int pageSize);
	
	public List<ProductHotBo> getProductHotBoList(Integer langId,Integer siteId,String currency);
}
