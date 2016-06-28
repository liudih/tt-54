package com.tomtop.product.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.Lists;
import com.tomtop.framework.core.utils.BeanUtils;
import com.tomtop.framework.core.utils.Result;
import com.tomtop.product.models.bo.CategoryLableBo;
import com.tomtop.product.models.bo.CategoryStorageBo;
import com.tomtop.product.models.dto.CategoryWebsiteWithNameDto;
import com.tomtop.product.models.vo.Category;
import com.tomtop.product.models.vo.CategoryInfoVo;
import com.tomtop.product.services.ICategoryService;
import com.tomtop.product.services.storage.IProductStorageService;

/**
 * 产品分类
 * 
 * @author lijun
 *
 */
@RestController
public class CategoryController {

	@Autowired
	ICategoryService service;

	@Autowired
	IProductStorageService productStorageService;

	private final static String CATEGORY_TYPE = "category";
	private final static String DETAIL_TYPE = "detail";

	/**
	 * 获取分类
	 * 
	 * @param level
	 *            要获取分类的层级,如果没有传该参数,那么默认去的第一级分类(1,2...)
	 * @param parentId
	 *            要获取parentId下面的直接子分类
	 * @param languageid
	 *            语言ID
	 * @param websiteid
	 *            站点ID
	 */
	@Cacheable(value = "product_category", keyGenerator = "customKeyGenerator")
	@RequestMapping(value = "/ic/v1/categories", method = RequestMethod.GET)
	public Result get(
			@RequestParam(value = "level", required = false) Integer level,
			@RequestParam(value = "parentId", required = false) Integer parentId,
			@RequestParam(value = "lang", required = false, defaultValue = "1") Integer languageid,
			@RequestParam(value = "client", required = false, defaultValue = "1") Integer websiteid) {
		List<Category> list = Lists.newArrayList();
		if (level != null) {
			List<CategoryWebsiteWithNameDto> result = this.service
					.getCategoriesByLevel(languageid, websiteid, level);
			list = Lists.transform(result,
					r -> BeanUtils.mapFromClass(r, Category.class));
		} else if (parentId != null) {
			List<CategoryWebsiteWithNameDto> result = this.service
					.getChildrensByParentId(languageid, websiteid, parentId);
			list = Lists.transform(result,
					r -> BeanUtils.mapFromClass(r, Category.class));
		} else {
			List<CategoryWebsiteWithNameDto> category = this.service.getAll(
					languageid, websiteid);
			// 开始构造Tree
			list = Lists.transform(category,
					p -> BeanUtils.mapFromClass(p, Category.class));
		}
		return new Result(Result.SUCCESS, list.toArray());
	}

	/**
	 * 根具类目id获取类目信息
	 * 
	 * @param categoryId
	 * @param languageid
	 * @param websiteid
	 * @return
	 */
	@Cacheable(value = "product_category", keyGenerator = "customKeyGenerator")
	@RequestMapping(value = "/ic/v1/categories/{categoryId}", method = RequestMethod.GET)
	public Result getByCategoryId(
			@PathVariable("categoryId") Integer categoryId,
			@RequestParam(value = "lang", required = false, defaultValue = "1") Integer languageid,
			@RequestParam(value = "client", required = false, defaultValue = "1") Integer websiteid) {

		CategoryWebsiteWithNameDto category = this.service
				.getCategoryByCategoryId(languageid, websiteid, categoryId);
		if (category != null) {
			return new Result(Result.SUCCESS, BeanUtils.mapFromClass(category,
					CategoryInfoVo.class));
		}
		return new Result(Result.FAIL, null);
	}

	/**
	 * 获取类目id信息展开的树(类目页用的左边导航)
	 * 
	 * @param categoryId
	 * @param lang
	 * @param client
	 * @return
	 */
	/*
	 * @RequestMapping(value = "/ic/v1/categoryShow", method =
	 * RequestMethod.GET) public Result getCategoryIdInfoTree(
	 * 
	 * @RequestParam("categoryId") int categoryId,
	 * 
	 * @RequestParam(value = "lang") int lang,
	 * 
	 * @RequestParam(value = "client") int client) { Integer parentId =
	 * service.getCategoryParentIdBycategoryId(client, categoryId);
	 * List<CategoryWebsiteWithNameDto> roots = service
	 * .getRootCategoriesByDisplay(lang, client, true); if (parentId != null) {
	 * List<Integer> parentIds = Lists.newArrayList(); parentIds.add(parentId);
	 * while (true) { parentId = service.getCategoryParentIdBycategoryId(client,
	 * parentId); if (parentId == null) { break; } else {
	 * parentIds.add(parentId); } } L ist<CategoryWebsiteWithNameDto> levels =
	 * null; parentIds.remove(parentIds.size() - 1); if (parentIds.size() > 0) {
	 * levels = service.getChildCategoriesAll(parentIds.get(0), lang, client);
	 * List<CategoryWebsiteWithNameDto> trees = service
	 * .getCategoryByCategoryIds(lang, client, parentIds); if (levels != null &&
	 * levels.size() > 0) { roots.addAll(levels); } if (trees.size() > 0) {
	 * roots.addAll(trees); } } } return new Result(Result.SUCCESS, roots); }
	 */
	/**
	 * 获取面包屑
	 * 
	 * @param categoryId
	 * @param lang
	 * @param type
	 * 
	 * @author renyy
	 * @return
	 */
	@Cacheable(value = "product", keyGenerator = "customKeyGenerator", cacheManager="dayCacheManager")
	@RequestMapping(method = RequestMethod.GET, value = "/ic/v1/bread/{id}")
	public Result getCategoryBreadCrumbs(
			@PathVariable("id") String id,
			@RequestParam(value = "lang", required = false, defaultValue = "1") Integer languageid,
			@RequestParam(value = "type", required = false, defaultValue = "category") String type) {

		List<CategoryLableBo> clbList = new ArrayList<CategoryLableBo>();
		if (CATEGORY_TYPE.equals(type)) {
			Integer categoryId = Integer.parseInt(id);
			clbList = service.getCategoryLableBreadCrumbs(categoryId,
					languageid);
		}
		if (DETAIL_TYPE.equals(type)) {
			clbList = service.getCategoryLableBreadCrumbsByListingId(id,
					languageid);
		}
		if (clbList.size() > 0) {
			return new Result(Result.SUCCESS, clbList.toArray());
		} else {
			return new Result(Result.FAIL, null);
		}
	}

	/**
	 * 获取Local Warehouse
	 * 
	 * @param lang
	 * @param client
	 * @param country
	 * 
	 * @author renyy
	 * @return
	 */
	@Cacheable(value = "product", keyGenerator = "customKeyGenerator")
	@RequestMapping(method = RequestMethod.GET, value = "/ic/v1/categories/storage")
	public Result getCategoryStorage(
			@RequestParam(value = "lang", required = false, defaultValue = "1") Integer languageid,
			@RequestParam(value = "client", required = false, defaultValue = "1") Integer websiteid,
			@RequestParam(value = "country", required = false, defaultValue = "US") String country) {

		List<CategoryStorageBo> csboList = productStorageService
				.getAllCategoryStorageBo();

		if (csboList != null && csboList.size() > 0) {
			return new Result(Result.SUCCESS, csboList.toArray());
		} else {
			return new Result(Result.FAIL, null);
		}
	}
}
