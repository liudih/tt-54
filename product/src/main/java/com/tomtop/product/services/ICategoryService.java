package com.tomtop.product.services;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.cache.annotation.Cacheable;

import com.tomtop.product.models.bo.CategoryLableBo;
import com.tomtop.product.models.dto.CategoryWebsiteWithNameDto;

public interface ICategoryService {

	/**
	 * 获取level层级可显示的类目
	 * 
	 * @param languageid
	 * @param websiteid
	 * @param level
	 * @return
	 */
	List<CategoryWebsiteWithNameDto> getCategoriesByLevel(int languageid,
			int websiteid, int level);

	/**
	 * 获取类目树
	 * 
	 * @param languageid
	 * @param websiteid
	 * @return
	 */
	List<CategoryWebsiteWithNameDto> getCategoryTree(int languageid,
			int websiteid);

	/**
	 * 获取categoryId对应的类目
	 * 
	 * @param languageid
	 * @param websiteid
	 * @param categoryId
	 * @return
	 */
	CategoryWebsiteWithNameDto getCategoryByCategoryId(int languageid,
			int websiteid, int categoryId);

	/**
	 * 获取多个categoryId对应的类目
	 * 
	 * @param languageids
	 * @param websiteid
	 * @param categoryId
	 * @return
	 */
	@Cacheable(value = "product_category", keyGenerator = "customKeyGenerator")
	List<CategoryWebsiteWithNameDto> getCategoryByCategoryIds(int languageid,
			int websiteid, List<Integer> categoryIds);

	/**
	 * 获取parentId下面的直接子类目
	 * 
	 * @param languageid
	 * @param websiteid
	 * @param parentId
	 * @return
	 */
	List<CategoryWebsiteWithNameDto> getChildrensByParentId(int languageid,
			int websiteid, int parentId);

	/**
	 * 获取所有的类目
	 * 
	 * @param languageid
	 * @param websiteid
	 * @return
	 */
	List<CategoryWebsiteWithNameDto> getAll(int languageid, int websiteid);

	/**
	 * 根具父id获取子类目
	 * 
	 * @param parentCategoryIds
	 * @param languageid
	 * @param websiteid
	 * @param display
	 * @return
	 */
	List<CategoryWebsiteWithNameDto> getMultiChildCategoriesByDisplay(
			@Param("list") List<Integer> parentCategoryIds, int languageid,
			int websiteid, boolean display);

	/**
	 * 获取所有 的子类目
	 * 
	 * @param parentCategoryId
	 * @param languageid
	 * @param websiteid
	 * @return
	 */
	List<CategoryWebsiteWithNameDto> getChildCategoriesAll(
			int parentCategoryId, int languageid, int websiteid);

	/**
	 * 获取父类目
	 * 
	 * @param websiteid
	 * @param categoryId
	 * @return
	 */
	Integer getCategoryParentIdBycategoryId(int websiteid, int categoryId);

	/**
	 * 获取所有的根类目
	 * 
	 * @param languageid
	 * @param websiteid
	 * @param display
	 * @return
	 */
	List<CategoryWebsiteWithNameDto> getRootCategoriesByDisplay(int languageid,
			int websiteid, boolean display);

	/**
	 * 根据categoryID获取面包屑
	 * 
	 * @param categoryId
	 * @param languageId
	 * @return
	 */
	List<CategoryLableBo> getCategoryLableBreadCrumbs(Integer categoryId,
			Integer languageId);

	/**
	 * 根据listingID获取面包屑
	 * 
	 * @param listingId
	 * @param languageId
	 * @return
	 */
	List<CategoryLableBo> getCategoryLableBreadCrumbsByListingId(
			String listingId, Integer languageId);

	/**
	 * 根据cpath获取面包屑
	 * 
	 * @param cpath
	 * @param languageId
	 * @return
	 */
	List<CategoryLableBo> getCategoryLableBreadByPath(String path,
			Integer languageId);

	/**
	 * 根据cpath获取categoryId
	 * 
	 * @return
	 */
	HashMap<String, Integer> getCategoryPath();
}
