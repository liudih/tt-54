package com.tomtop.product.services;

import java.util.List;

import com.tomtop.product.models.bo.PrdouctDescBo;
import com.tomtop.product.models.bo.ProductAttributeBo;
import com.tomtop.product.models.bo.ProductDetailsBo;
import com.tomtop.product.models.bo.ProductPriceBo;
import com.tomtop.product.models.bo.ProductSeoBo;

public interface IProductDetailService {

	public List<ProductDetailsBo> getProductDetailsBoList(String listingId,
			Integer langId, Integer siteId, String currency);

	public PrdouctDescBo getPrdouctDescBo(String listingId, Integer langId,
			Integer siteId);

	public ProductSeoBo getProductSeoBo(String listingId, Integer langId,
			Integer siteId);

	public ProductPriceBo getProductBasePriceBo(String listingId,
			Integer langId, Integer siteId, String currency);

	/**
	 * 获取产品的属性
	 * 
	 * @param listingIds
	 *            商品id
	 * @param langId
	 *            语言
	 * @param siteId
	 *            站点
	 * @return
	 */
	public List<ProductAttributeBo> getProductAttributeDtoByListingIds(
			List<String> listingIds, Integer langId, Integer siteId);
	
	public Integer getProductQty(String listingId);
}
