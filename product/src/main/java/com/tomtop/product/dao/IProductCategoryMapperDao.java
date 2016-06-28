package com.tomtop.product.dao;

import java.util.List;

/**
 * 产品类目关系
 * 
 * @author liulj
 *
 */
public interface IProductCategoryMapperDao {

	/**
	 * 获取商品的父类目Id
	 * 
	 * @param listingid
	 * @return
	 */
	List<Integer> getListingCategoryParentIdByListingId(String listingid);

	/**
	 * 获取类目id的所有listingID
	 * 
	 * @param rootCategoryIds
	 *            类目id
	 * @return
	 */
	List<String> getAllListingIdsByRootIds(List<Integer> rootCategoryIds);

	/**
	 * 获取所有的listingid
	 * 
	 * @param categoryIds
	 * @param client
	 *            商品站点
	 * @param status
	 *            商品状态
	 * @return
	 */
	List<String> getAllListingIdsByRootIds(List<Integer> rootCategoryIds,
			int client, int status);

	/**
	 * 获取产品类目的所有参数
	 * 
	 * @param categoryId
	 * @param pageSize
	 * @param pageNum
	 * @param client
	 * @param status
	 *            商品的状态
	 * @param attrIds
	 *            属性值的id列表
	 * @return
	 */
	List<String> getListingIdsByCategoryId(List<Integer> categoryId,
			Integer pageSize, Integer pageNum, int client, int status,
			List<Integer> attrIds);

	/**
	 * 获取产品类目的所有参数
	 * 
	 * @param rootCategoryId
	 * @param pageSize
	 * @param pageNum
	 * @return
	 */
	List<String> getListingIdsByCategoryId(List<Integer> categoryId,
			int client, int status);

	/**
	 * 获取类目的产品总数
	 * 
	 * @param categoryId
	 *            类目id
	 * @param client
	 * @param status
	 * @param attrIds
	 *            属性值的id列表
	 * @return
	 */
	int getListingIdCountByCategoryId(List<Integer> categoryId, int client,
			int status, List<Integer> attrIds);
}
