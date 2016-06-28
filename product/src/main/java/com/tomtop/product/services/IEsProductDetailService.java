package com.tomtop.product.services;

import java.util.List;

import com.tomtop.product.models.bo.CollectCountBo;
import com.tomtop.product.models.bo.PrdouctDescBo;
import com.tomtop.product.models.bo.ProductDetailsBo;
import com.tomtop.product.models.bo.ProductHotBo;
import com.tomtop.product.models.bo.ProductPriceBo;
import com.tomtop.product.models.bo.ProductSeoBo;
import com.tomtop.product.models.bo.ProductStorageBo;
import com.tomtop.product.models.bo.ReviewStartNumBo;
import com.tomtop.product.models.vo.ProductBaseDtlVo;

public interface IEsProductDetailService {
	
	public ProductBaseDtlVo getProductBaseDtlVo(String key,int langId, int siteId, String currency);

	public List<ProductDetailsBo> getProductDetailsBoList(String sku,
			Integer langId, Integer siteId, String currency);

	public PrdouctDescBo getPrdouctDescBo(String sku, Integer langId,
			Integer siteId);

	public ProductSeoBo getProductSeoBo(String sku, Integer langId,
			Integer siteId);
	
	public ProductPriceBo getProductBasePriceBo(String listingId,
			Integer langId, Integer siteId, String currency);
			
	public ReviewStartNumBo getReviewStartNumBoById(String listingId,Integer langId,
			Integer siteId);
	
	public CollectCountBo getCollectCountByListingId(String listingIds,Integer langId,
			Integer siteId);
	
	public List<ProductStorageBo> getProductStorage(String listingId,Integer qty, Integer langId,
			Integer siteId, String currency,String country);
	
	public List<ProductHotBo> getProductHotBoList(Integer langId, Integer siteId,String currency);
}
