package com.tomtop.product.services.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.google.common.collect.Maps;
import com.tomtop.product.dao.ICategoryDao;
import com.tomtop.product.mappers.product.CategoryMapper;
import com.tomtop.product.models.bo.CategoryLableBo;
import com.tomtop.product.models.dto.CategoryPathDto;
import com.tomtop.product.models.dto.CategoryWebsiteWithNameDto;
import com.tomtop.product.models.dto.category.CategoryLable;
import com.tomtop.product.services.ICategoryService;

/**
 * 类目服务实现类
 * 
 * @author lijun
 *
 */
@Service("categoryService")
public class CategoryService implements ICategoryService {

	// private static final Logger logger =
	// LoggerFactory.getLogger(CategoryService.class);

	@Autowired
	ICategoryDao dao;

	@Autowired
	CategoryMapper categoryMapper;

	@Override
	public List<CategoryWebsiteWithNameDto> getCategoriesByLevel(
			int languageid, int websiteid, int level) {
		Map<String, Object> paras = Maps.newLinkedHashMap();
		paras.put("langId", languageid);
		paras.put("websiteId", websiteid);
		paras.put("level", level);

		List<CategoryWebsiteWithNameDto> result = this.dao.getCategories(paras);

		return result;
	}

	@Override
	public List<CategoryWebsiteWithNameDto> getCategoryTree(int languageid,
			int websiteid) {
		// 目前没有bo层 所以先返回null
		return null;
	}

	@Override
	public List<CategoryWebsiteWithNameDto> getChildrensByParentId(
			int languageid, int websiteid, int parentId) {
		Map<String, Object> paras = Maps.newLinkedHashMap();
		paras.put("langId", languageid);
		paras.put("websiteId", websiteid);
		paras.put("parentId", parentId);

		List<CategoryWebsiteWithNameDto> result = this.dao.getCategories(paras);

		return result;
	}

	@Override
	public CategoryWebsiteWithNameDto getCategoryByCategoryId(int languageid,
			int websiteid, int categoryId) {
		Map<String, Object> paras = Maps.newLinkedHashMap();
		paras.put("langId", languageid);
		paras.put("websiteId", websiteid);
		paras.put("categoryId", categoryId);

		List<CategoryWebsiteWithNameDto> categories = this.dao
				.getCategories(paras);

		if (categories != null && categories.size() > 0) {
			CategoryWebsiteWithNameDto first = categories.get(0);
			return first;
		}
		return null;
	}

	@Override
	public List<CategoryWebsiteWithNameDto> getAll(int languageid, int websiteid) {
		Map<String, Object> paras = Maps.newLinkedHashMap();
		paras.put("langId", languageid);
		paras.put("websiteId", websiteid);

		List<CategoryWebsiteWithNameDto> result = this.dao.getCategories(paras);

		return result;
	}

	@Override
	public List<CategoryWebsiteWithNameDto> getCategoryByCategoryIds(
			int languageid, int websiteid, List<Integer> categoryIds) {
		return dao.getCategoriesByCategoryIds(categoryIds, languageid,
				websiteid);
	}

	@Override
	public List<CategoryWebsiteWithNameDto> getMultiChildCategoriesByDisplay(
			List<Integer> parentCategoryIds, int languageid, int websiteid,
			boolean display) {
		return dao.getMultiChildCategoriesByDisplay(parentCategoryIds,
				languageid, websiteid, display);
	}

	@Override
	public List<CategoryWebsiteWithNameDto> getChildCategoriesAll(
			int parentCategoryId, int languageid, int websiteid) {
		return dao.getChildCategoriesAll(parentCategoryId, languageid,
				websiteid);
	}

	@Override
	public Integer getCategoryParentIdBycategoryId(int websiteid, int categoryId) {
		return dao.getCategoryParentIdBycategoryId(websiteid, categoryId);
	}

	@Override
	public List<CategoryWebsiteWithNameDto> getRootCategoriesByDisplay(
			int languageid, int websiteid, boolean display) {
		return dao.getRootCategoriesByDisplay(languageid, websiteid, display);
	}

	public List<CategoryLableBo> getCategoryLableBreadCrumbs(
			Integer categoryId, Integer languageId) {
		List<CategoryLableBo> clist = new ArrayList<CategoryLableBo>();
		CategoryLable cl = categoryMapper.getCategoryLable(categoryId,
				languageId);
		if (cl == null) {
			return clist;
		}
		CategoryLableBo clbo = new CategoryLableBo();
		clbo.setCategoryId(cl.getIcategoryid());
		clbo.setName(cl.getCname());
		clbo.setLevel(cl.getIlevel());
		clbo.setCpath(cl.getCpath());
		clist.add(clbo);
		Integer parentid = cl.getIparentid();
		if (parentid != null) {
			do {
				cl = categoryMapper.getCategoryLable(parentid, languageId);
				clbo = new CategoryLableBo();
				clbo.setCategoryId(cl.getIcategoryid());
				clbo.setName(cl.getCname());
				clbo.setLevel(cl.getIlevel());
				clbo.setCpath(cl.getCpath());
				clist.add(clbo);
				parentid = cl.getIparentid();
			} while (parentid != null);
		}
		return clist;
	}

	@Override
	public List<CategoryLableBo> getCategoryLableBreadCrumbsByListingId(
			String listingId, Integer languageId) {
		List<CategoryLableBo> clbolist = new ArrayList<CategoryLableBo>();
		List<CategoryLable> clist = categoryMapper.getCategoryLableByListingId(
				listingId, languageId);
		if (clist == null || clist.size() == 0) {
			return clbolist;
		}
		CategoryLableBo clbo = null;
		CategoryLable cl = null;
		for (int i = 0; i < clist.size(); i++) {
			cl = clist.get(i);
			clbo = new CategoryLableBo();
			clbo.setCategoryId(cl.getIcategoryid());
			clbo.setName(cl.getCname());
			clbo.setLevel(cl.getIlevel());
			clbo.setCpath(cl.getCpath());
			clbolist.add(clbo);
		}
		return clbolist;
	}
	
	public List<CategoryLableBo> getCategoryLableBreadByPath(
			String path, Integer languageId) {
		List<CategoryLableBo> clist = new ArrayList<CategoryLableBo>();
		CategoryLable cl = categoryMapper.getCategoryLableByPath(path,
				languageId);
		if (cl == null) {
			return clist;
		}
		CategoryLableBo clbo = new CategoryLableBo();
		clbo.setCategoryId(cl.getIcategoryid());
		clbo.setName(cl.getCname());
		clbo.setLevel(cl.getIlevel());
		clist.add(clbo);
		Integer parentid = cl.getIparentid();
		if (parentid != null) {
			do {
				cl = categoryMapper.getCategoryLable(parentid, languageId);
				clbo = new CategoryLableBo();
				clbo.setCategoryId(cl.getIcategoryid());
				clbo.setName(cl.getCname());
				clbo.setLevel(cl.getIlevel());
				clist.add(clbo);
				parentid = cl.getIparentid();
			} while (parentid != null);
		}
		return clist;
	}

	@Cacheable(value = "category_path", keyGenerator = "customKeyGenerator",cacheManager = "dayCacheManager")
	@Override
	public HashMap<String, Integer> getCategoryPath() {
		HashMap<String, Integer> cidMap = new HashMap<String, Integer>();
		List<CategoryPathDto> cpList = categoryMapper.getCategoryPathDto();
		CategoryPathDto cpdto = null;
		String key = "";
		Integer value;
		for (int i = 0; i < cpList.size(); i++) {
			cpdto = cpList.get(i);
			key = cpdto.getCpath().trim();
			value = cpdto.getIid();
			cidMap.put(key, value);
		}
		return cidMap;
	}
}
