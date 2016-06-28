package com.tomtop.product.services;

import java.util.List;

import com.tomtop.product.models.bo.ProductBasePriceReviewCollectInfoBo;

/**
 * 从搜索引擎读数据
 * 
 * @author liulj
 *
 */
public interface IProductEsDataService extends IProductBaseInfoService {
	public ProductBasePriceReviewCollectInfoBo getDataByListingId(
			String listingId, Integer lang, Integer client, String currency);

	public List<ProductBasePriceReviewCollectInfoBo> getDataByListingIds(
			List<String> listingIds, Integer lang, Integer client,
			String currency);

	public ProductBasePriceReviewCollectInfoBo getDataByListingId(
			String listingId, Integer lang, Integer client);

	public List<ProductBasePriceReviewCollectInfoBo> getDataByListingIds(
			List<String> listingIds, Integer lang, Integer client);
	
	public List<ProductBasePriceReviewCollectInfoBo> getBouthtProduct(
			String listingId,Integer client, Integer lang,String currency);
	
	public List<ProductBasePriceReviewCollectInfoBo> getViewedProduct(
			String listingId,Integer client, Integer lang,String currency);
	
	public List<ProductBasePriceReviewCollectInfoBo> getRecommendProduct(
			String listingId,Integer client, Integer lang,String currency);
	
}
