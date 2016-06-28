package com.tomtop.product.dao.impl;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.tomtop.product.dao.ICategoryDao;
import com.tomtop.product.mappers.product.CategoryWebsiteMapper;
import com.tomtop.product.models.dto.CategoryWebsiteWithNameDto;

@Repository
public class CategoryDao implements ICategoryDao {

	@Autowired
	CategoryWebsiteMapper mapper;

	@Override
	public List<CategoryWebsiteWithNameDto> getCategories(
			Map<String, Object> paras) {
		if (paras == null) {
			return null;
		}
		return this.mapper.getCategories(paras);
	}

	@Override
	public List<CategoryWebsiteWithNameDto> getCategoriesByCategoryIds(
			List<Integer> ids, int languageid, int websiteid) {
		// TODO Auto-generated method stub
		return ids != null && ids.size() > 0 ? mapper
				.getCategoriesByCategoryIds(ids, languageid, websiteid)
				: Arrays.asList();
	}

	public List<CategoryWebsiteWithNameDto> getMultiChildCategoriesByDisplay(
			List<Integer> parentCategoryIds, int languageid, int websiteid,
			boolean display) {
		return mapper.getMultiChildCategoriesByDisplay(parentCategoryIds,
				languageid, websiteid, display);
	}

	@Override
	public List<CategoryWebsiteWithNameDto> getChildCategoriesAll(
			int parentCategoryId, int languageid, int websiteid) {
		// TODO Auto-generated method stub
		return mapper.getChildCategoriesAll(parentCategoryId, languageid,
				websiteid);
	}

	@Override
	public Integer getCategoryParentIdBycategoryId(int websiteid, int categoryId) {
		// TODO Auto-generated method stub
		return mapper.getCategoryParentIdBycategoryId(websiteid, categoryId);
	}

	@Override
	public List<CategoryWebsiteWithNameDto> getRootCategoriesByDisplay(
			int languageid, int websiteid, boolean display) {
		// TODO Auto-generated method stub
		return mapper
				.getRootCategoriesByDisplay(languageid, websiteid, display);
	}
}
