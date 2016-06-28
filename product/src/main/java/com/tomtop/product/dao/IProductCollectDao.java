package com.tomtop.product.dao;

import java.util.List;

import com.tomtop.product.models.dto.CollectCountDto;
import com.tomtop.product.models.dto.ProductCollectDto;

/**
 * 产品收藏
 * 
 * @author liulj
 *
 */
public interface IProductCollectDao {
	/**
	 * 获取收藏数
	 * 
	 * @param listingIds
	 * @return
	 */
	public List<CollectCountDto> getCollectCountByListingIds(
			List<String> listingIds);
	/**
	 * 获取单个商品的收藏数
	 * 
	 * @param listingIds
	 * @return
	 */
	public CollectCountDto getCollectCountByListingId(
			String listingIds);
	
	/**
	 * 添加收藏
	 * 
	 * @param ProductCollectDto
	 * 
	 * 
	 * @return
	 */
	public Integer addProductCollectDto(ProductCollectDto pcdto);
	
	/**
	 * 用户收藏商品
	 * 
	 * @param listingId
	 * 
	 * @param email
	 * 
	 * @return
	 */
	public ProductCollectDto getProductCollectDtoByEmailAndListingId(String listingId,String email);
	
	/**
	 * 获取用户收藏的商品集合
	 */
	public List<String> getCollectByEmail(String email);
}