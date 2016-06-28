package com.tomtop.product.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.tomtop.product.models.dto.CategoryWebsiteWithNameDto;

/**
 * 类目dao
 * 
 * @author lijun
 *
 */
public interface ICategoryDao {

	/**
	 * 获取类目
	 * 
	 * @param paras
	 * @return
	 */
	List<CategoryWebsiteWithNameDto> getCategories(Map<String, Object> paras);

	/**
	 * 根具多个类目id获取类目
	 * 
	 * @param ids
	 *            id数组
	 * @param languageid
	 * @param websiteid
	 * @return
	 */
	List<CategoryWebsiteWithNameDto> getCategoriesByCategoryIds(
			@Param("list") List<Integer> ids, int languageid, int websiteid);

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
}
