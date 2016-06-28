package controllers.mobile.product;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import play.Logger;
import play.libs.Json;
import play.mvc.Result;
import services.mobile.MobileService;
import services.mobile.product.CategoryService;
import services.mobile.product.ProductService;
import services.product.CategoryEnquiryService;
import services.product.ProductEnquiryService;
import services.search.ISearchService;
import services.search.SearchContextFactory;
import valuesobject.mobile.BaseJson;
import valuesobject.mobile.BaseListJson;
import valuesobject.mobile.BasePageJson;
import valuesobject.mobile.BaseResultType;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.website.dto.category.WebsiteCategory;

import controllers.mobile.TokenController;
import dto.mobile.CategoryCompositeInfo;
import dto.mobile.CategoryInfo;
import dto.mobile.ProductLiteInfo;

public class CategoryController extends TokenController {

	@Inject
	CategoryEnquiryService categoryEnquiryService;

	@Inject
	CategoryService categoryService;

	@Inject
	SearchContextFactory searchFactory;

	@Inject
	ISearchService genericSearch;

	@Inject
	ProductEnquiryService productEnquiryService;

	@Inject
	ProductService productService;

	@Inject
	MobileService mobileService;

	/**
	 * 获取所有商品类别
	 * 
	 * @return
	 */
	public Result getAllCategorys() {
		try {
			Integer websiteid = mobileService.getWebSiteID();
			Integer languageid = mobileService.getLanguageID();
			List<WebsiteCategory> categorys = categoryEnquiryService
					.getAllCategories(websiteid, languageid);
			if (categorys != null && categorys.size() > 0) {
				List<CategoryInfo> resultList = Lists.transform(categorys,
						new Function<WebsiteCategory, CategoryInfo>() {
							@Override
							public CategoryInfo apply(WebsiteCategory category) {
								return new CategoryInfo(category.getParentId(),
										category.getName(), category
												.getCategoryId(), category
												.getLevel());
							}
						});
				BaseListJson<CategoryInfo> result = new BaseListJson<CategoryInfo>();
				result.setRe(BaseResultType.SUCCESS);
				result.setMsg(BaseResultType.SUCCESSMSG);
				result.setList(resultList);
				return ok(Json.toJson(result));
			}
		} catch (Exception e) {
			Logger.error("Exception", e);
			BaseJson result = new BaseJson();
			result.setRe(BaseResultType.ERROR);
			result.setMsg(BaseResultType.EXCEPTIONMSG);
			return ok(Json.toJson(result));
		}
		BaseJson result = new BaseJson();
		result.setRe(BaseResultType.ERROR);
		result.setMsg(BaseResultType.NODATA);
		return ok(Json.toJson(result));
	}

	/**
	 * 查询所有大类别
	 * 
	 * @param websiteid
	 * @param languageid
	 * @return
	 */
	public Result getRootCategorys(int max) {
		try {
			Integer websiteid = mobileService.getWebSiteID();
			Integer languageid = mobileService.getLanguageID();
			List<CategoryInfo> resultList = categoryService.getRootCategorys(
					max, websiteid, languageid);
			if (resultList != null && resultList.size() > 0) {
				BaseListJson<CategoryInfo> result = new BaseListJson<CategoryInfo>();
				result.setRe(BaseResultType.SUCCESS);
				result.setMsg(BaseResultType.SUCCESSMSG);
				result.setList(resultList);
				return ok(Json.toJson(result));
			}
		} catch (Exception p) {
			Logger.error("Exception", p);
			BaseJson result = new BaseJson();
			result.setRe(BaseResultType.EXCEPTION);
			result.setMsg(BaseResultType.EXCEPTIONMSG);
			return ok(Json.toJson(result));
		}
		BaseJson result = new BaseJson();
		result.setRe(BaseResultType.ERROR);
		result.setMsg(BaseResultType.NODATA);
		return ok(Json.toJson(result));
	}

	/**
	 * 查询子分类
	 * 
	 * @param categoryId
	 * @return
	 */
	public Result getCategorysByParentId(int cid, int max, int depth) {
		try {
			Integer websiteid = mobileService.getWebSiteID();
			Integer languageid = mobileService.getLanguageID();
			List<CategoryCompositeInfo> resultList = categoryService
					.getCategorysByParentId(cid, max, depth, websiteid,
							languageid);
			if (resultList != null && resultList.size() > 0) {
				BaseListJson<CategoryCompositeInfo> result = new BaseListJson<CategoryCompositeInfo>();
				result.setRe(BaseResultType.SUCCESS);
				result.setMsg(BaseResultType.SUCCESSMSG);
				result.setList(resultList);
				return ok(Json.toJson(result));
			}
		} catch (Exception e) {
			Logger.error("Exception", e);
			BaseJson result = new BaseJson();
			result.setRe(BaseResultType.EXCEPTION);
			result.setMsg(BaseResultType.EXCEPTIONMSG);
			return ok(Json.toJson(result));
		}
		BaseJson result = new BaseJson();
		result.setRe(BaseResultType.ERROR);
		result.setMsg(BaseResultType.NODATA);
		return ok(Json.toJson(result));
	}

	/**
	 * 查询某一类别的商品列表
	 * 
	 * @return
	 */
	public Result showCategoryProduct(Integer cid, Integer p, Integer size) {
		try {
			Map<String, String[]> queryStrings = request().queryString();
			utils.Page<ProductLiteInfo> resultList = productService
					.getCategoryProducts(cid, p, size, queryStrings);
			if (resultList != null && resultList.getList().size() > 0) {
				BasePageJson<ProductLiteInfo> result = new BasePageJson<ProductLiteInfo>();
				result.setRe(BaseResultType.SUCCESS);
				result.setMsg(BaseResultType.SUCCESSMSG);
				result.setTotal(resultList.getTotal());
				result.setP(p);
				result.setSize(size);
				result.setList(resultList.getList());
				return ok(Json.toJson(result));
			}
		} catch (Exception e) {
			Logger.error("Exception", e);
			BaseJson result = new BaseJson();
			result.setRe(BaseResultType.EXCEPTION);
			result.setMsg(BaseResultType.EXCEPTIONMSG);
			return ok(Json.toJson(result));
		}
		BaseJson result = new BaseJson();
		result.setRe(BaseResultType.ERROR);
		result.setMsg(BaseResultType.NODATA);
		return ok(Json.toJson(result));
	}
}
