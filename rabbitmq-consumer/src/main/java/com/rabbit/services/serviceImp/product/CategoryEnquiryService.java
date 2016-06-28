package com.rabbit.services.serviceImp.product;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.rabbit.conf.mapper.category.FilterAttributeValueMapper;
import com.rabbit.conf.mapper.product.CategoryBaseMapper;
import com.rabbit.conf.mapper.product.CategoryNameMapper;
import com.rabbit.conf.mapper.product.CategoryWebsiteMapper;
import com.rabbit.dto.product.CategoryName;
import com.rabbit.dto.product.CategoryWebsiteWithName;
import com.rabbit.dto.transfer.category.CategoryAttribute;
import com.rabbit.dto.transfer.category.WebsiteCategory;
import com.rabbit.services.iservice.product.ICategoryEnquiryService;

@Service
public class CategoryEnquiryService implements ICategoryEnquiryService {
	// private static Logger
	// log=Logger.getLogger(CategoryEnquiryService.class.getName());

	@Autowired
	CategoryWebsiteMapper cpmapper;
	/* @CacheResult("category") */
	@Autowired
	FilterAttributeValueMapper filterAttributeValueMapper;
	@Autowired
	CategoryBaseMapper categoryBaseMapper;
	@Autowired
	CategoryNameMapper categoryNameMapper;

	@Override
	public List<WebsiteCategory> getAllCategories(int siteid, int languageid) {
		List<CategoryWebsiteWithName> list = cpmapper.getCategories(languageid,
				siteid);
		return Lists.transform(list, obj -> {
			WebsiteCategory citem = new WebsiteCategory();
			citem.setId(obj.getIid());
			citem.setContent(obj.getCcontent());
			citem.setLanguageId(languageid);
			citem.setLevel(obj.getIlevel());
			citem.setName(obj.getCname());
			citem.setParentId(obj.getIparentid());
			citem.setPath(obj.getCpath());
			citem.setPosition(obj.getIposition());
			citem.setWebsiteId(obj.getIwebsiteid());
			citem.setCategoryId(obj.getIcategoryid());
			return citem;
		});
	}

	public List<Integer> getCategoryIds(String categoryPath) {
		return categoryBaseMapper.getFullCategoryIds(categoryPath);
	}

	/* @CacheResult("category") */
	public List<CategoryAttribute> getAllCategoryAttributes() {
		return filterAttributeValueMapper.selectAll();
	}

	public CategoryName getCategoryNameByCategoryIdAndLanguage(
			Integer categoryId, Integer language) {
		return categoryNameMapper.getCategoryNameByCategoryIdAndLanguage(
				categoryId, language);
	}

	/**
	 * loop and query the db for all parent categories (excluding itself)
	 *
	 * @param categoryId
	 * @param language
	 * @param siteId
	 * @return
	 */
	/* @CacheResult("category") */
	public List<CategoryWebsiteWithName> getAllParentCategories(
			final int categoryId, final int language, final int siteId) {
		List<CategoryWebsiteWithName> all = Lists.newArrayList();
		Integer icategoryId = categoryId;
		while (icategoryId != null) {
			CategoryWebsiteWithName pf = cpmapper.getCategory(icategoryId,
					language, siteId);
			if (pf == null) {
				break;
			}
			if (pf.getIcategoryid() != categoryId) {
				all.add(pf);
			}
			icategoryId = pf.getIparentid();
		}
		return all;
	}

	public List<CategoryWebsiteWithName> getChildCategoriesByBshow(
			final int categoryId, final int language, final int siteId) {

		List<CategoryWebsiteWithName> pf = cpmapper.getChildCategoriesByBshow(
				categoryId, language, siteId);

		return pf;
	}

	public CategoryWebsiteWithName getCategoryWebsiteById(final int categoryId) {

		CategoryWebsiteWithName pf = cpmapper
				.getCategoryWebsiteById(categoryId);

		return pf;
	}

	public CategoryWebsiteWithName getCategoryWebsiteByIdAndSiteIdAndLangId(
			final int categoryId, int langId, int websiteId) {

		CategoryWebsiteWithName pf = cpmapper
				.getCategoryWebsiteByIdAndSiteIdAndLangId(categoryId, langId,
						websiteId);

		return pf;
	}

	public List<CategoryWebsiteWithName> getCategoriesByCategoryIds(
			List<Integer> ids, int languageid, int websiteid){
		return cpmapper.getCategoriesByCategoryIds(ids, languageid, websiteid);
	}
}
