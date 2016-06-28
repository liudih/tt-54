package com.tomtop.product.services;

import java.util.List;

import com.tomtop.product.models.bo.BaseBo;
import com.tomtop.product.models.bo.CollectCountBo;

/**
 * 产品收藏
 * 
 * @author liulj
 *
 */
public interface IProductCollectService {
	/**
	 * 获取收藏数
	 * 
	 * @param listingIds
	 * @return
	 */
	public List<CollectCountBo> getCollectCountByListingIds(
			List<String> listingIds);
	
	/**
	 * 获取单个商品收藏数
	 * 
	 * @param listingIds
	 * @return
	 */
	public CollectCountBo getCollectCountByListingId(
			String listingIds);
	
	/**
	 * 添加收藏
	 * 
	 * @param listingIds
	 * @return
	 */
	public BaseBo addCollectCount(String listingId,String email);
	
	/**
	 * 根据邮箱获取收藏的listingId 集合
	 * 
	 * @param listingIds
	 * @return
	 */
	public List<String> getCollectListingIdByEmail(String email);
}