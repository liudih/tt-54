package controllers.product;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import play.Logger;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http.Context;
import play.mvc.Result;
import services.base.FoundationService;
import services.product.ICategoryEnquiryService;
import services.product.IProductBadgeService;
import services.product.ProductUtilService;
import services.search.ISearchContextFactory;
import services.search.ISearchService;
import services.search.criteria.CategorySearchCriteria;
import valueobjects.base.Page;
import valueobjects.product.ProductBadge;
import valueobjects.search.SearchContext;

import com.google.common.collect.Sets;
import com.google.inject.Inject;

import context.ContextUtils;
import context.WebContext;
import controllers.base.Home;
import dto.product.CategoryName;
import dto.product.CategoryWebsiteWithName;

public class Category extends Controller {

	@Inject
	ICategoryEnquiryService categoryEnquiryService;

	@Inject
	ISearchService genericSearch;

	@Inject
	ISearchContextFactory searchFactory;

	@Inject
	FoundationService foundation;

	@Inject
	IProductBadgeService badgeService;

	@Inject
	ProductUtilService productUtilService;

	public Result categoies() {

		return ok(views.html.category.index.render());
	}

	public Result subCategories(final String cpath, final Integer page,
			final Integer size, final String filter, Integer st) {
		String ckey = cpath;
		try {
			ckey = URLDecoder.decode(cpath, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			ckey = cpath;
		}
		int lang = foundation.getLanguage();
		int site = foundation.getSiteID();
		String currency = foundation.getCurrency();
		String device = foundation.getDevice();
		
		WebContext webContex = ContextUtils.getWebContext(Context.current());
		// --- meta preparation
		CategoryName cn = categoryEnquiryService.getCategoryNameByPath(cpath, webContex);
		if (cn == null) {
			return Home.notFoundResult();
		}

		Map<String, String[]> queryStrings = request().queryString();
		SearchContext sc = searchFactory.fromQueryString(
				new CategorySearchCriteria(cn.getIcategoryid()), queryStrings, null);
		
//		SearchContext sc = searchFactory.fromQueryString(null, request()
//				.queryString(), Sets.newHashSet());
		sc.setPageSize(12);
		Page<String> listingids = genericSearch.search(sc, site, lang);
		Page<ProductBadge> blist = listingids.batchMap(list -> badgeService
				.getProductBadgesByListingIDs(list, lang, site, currency, null,
						false, false));
		productUtilService.addProductBadgeCollect(blist.getList(),
				listingids.getList(), lang, site, currency);
		// 产品加入评论
		productUtilService.addReview(blist.getList(), listingids.getList(),
				lang, site, currency);

		// 从更多按钮进来的
		String ismore = request().getQueryString("ismore");
		if (ismore != null && "1".equals(ismore)) {
			Map<String, Object> mjson = new HashMap<String, Object>();
			StringBuilder html = new StringBuilder();
			for (ProductBadge b : blist.getList()) {
				html.append(views.html.product.badge_base.render(b).toString());
			}
			mjson.put("html", html.toString());
			mjson.put("totlePage", blist.totalPages());
			mjson.put("page", blist.pageNo() + 1);
			return ok(Json.toJson(mjson));
		}

		List<CategoryWebsiteWithName> clist = categoryEnquiryService
				.getChildCategories(cn.getIcategoryid(), webContex);
		return ok(views.html.category.sub_category.render(clist, blist,
				cn.getIcategoryid(), request().queryString(), cpath));
	}
}
