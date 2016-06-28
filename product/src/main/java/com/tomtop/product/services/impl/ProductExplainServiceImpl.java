package com.tomtop.product.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.tomtop.product.mappers.product.ProductExplainMapper;
import com.tomtop.product.models.dto.ProductExplainDto;
import com.tomtop.product.services.IProductExplainService;

@Service
public class ProductExplainServiceImpl implements IProductExplainService {

	@Autowired
	ProductExplainMapper productExplainMapper;
	
	/**
	 * 获取商品的explanin
	 * 
	 * @param type
	 * 
	 * @param langId
	 * 
	 * @param siteId
	 * 
	 * @return String
	 * @author renyy
	 */
	@Cacheable(value = "product_explain", keyGenerator = "customKeyGenerator")
	@Override
	public String getProductExplainByType(String type, Integer siteId,
			Integer langId) {
		String context = "";
		if(type == null || "".equals(type.trim())){
			return context;
		}
		ProductExplainDto pedto = productExplainMapper.getProductExplainByType(type, siteId, langId);
		if(pedto == null || pedto.getCcontent() == null){
			return context;
		}
		context = pedto.getCcontent();
		return context;
	}
}
