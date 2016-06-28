package com.tomtop.product.services;

import java.util.List;

import com.tomtop.product.models.bo.ProductBasePriceReviewCollectInfoBo;

/**
 * 产品服务工具类
 * 
 * @author liulj
 *
 */
public interface IProductBaseInfoService {

	/**
	 * 获取产品信息
	 * 
	 * @param listings
	 * @param client
	 * @param lang
	 * @return
	 */
	public List<ProductBasePriceReviewCollectInfoBo> getProductBaseByListings(
			List<String> listings, int client, int lang);

	public List<ProductBasePriceReviewCollectInfoBo> getProductBasePriceByListings(
			List<String> listings, String currency, int client, int lang);

	public ProductBasePriceReviewCollectInfoBo getProductBasePriceByListing(
			String listing, String currency, int client, int lang);

	public List<ProductBasePriceReviewCollectInfoBo> getProductBasePriceReviewCollectByListings(
			List<String> listings, String currency, int client, int lang);

	public List<ProductBasePriceReviewCollectInfoBo> getProductBasePriceReviewByListings(
			List<String> listings, String currency, int client, int lang);

	/**
	 * 获取排序的list产品根具类目Id
	 * 
	 * @param categoryId
	 *            类目Id
	 * @param sequence
	 *           1 - 根据30天内销量降序，上架时间升序
	 * 		     2 - 根据上架时间升序，30天内销量降序
	 * @param client
	 * @param lang
	 * @return
	 */
	public List<ProductBasePriceReviewCollectInfoBo> getSortListByCategoryId(
			Integer categoryId, Integer sequence, Integer client, Integer lang,
			String currency);

	/**
	 * 获取你可能也喜欢的产品
	 * 
	 * @param key
	 *            聚合关键字，目前为listingId
	 * @param client
	 * @param lang
	 * @param currency
	 * @return
	 */
	public List<ProductBasePriceReviewCollectInfoBo> getSearchProductLikeList(
			String key, Integer client, Integer lang, String currency);
}