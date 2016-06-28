package controllers.product;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import play.Logger;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http.Context;
import play.mvc.Result;
import play.twirl.api.Html;
import services.dodocool.product.ProductMessageService;
import services.product.ICategoryEnquiryService;
import services.product.IProductCategoryEnquiryService;
import services.search.ISearchContextFactory;
import services.search.ISearchService;
import services.search.criteria.CategorySearchCriteria;
import valueobjects.base.Page;
import valueobjects.search.SearchContext;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.api.client.util.Lists;
import com.google.common.collect.Collections2;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.inject.Inject;

import context.ContextUtils;
import context.WebContext;
import controllers.base.Home;
import dto.product.CategoryWebsiteWithName;
import dto.product.ProductMessage;

public class CategoryProduct extends Controller {
	@Inject
	ICategoryEnquiryService categoryEnquiryService;

	@Inject
	IProductCategoryEnquiryService productCategoryService;

	@Inject
	ProductMessageService productMessageService;

	@Inject
	ISearchContextFactory factory;

	@Inject
	ISearchService searchService;

	public Result showCategoryProduct(final String cpath) {
		Logger.debug("---------------cpath="+JSON.toJSONString(cpath));
		WebContext webContext = ContextUtils.getWebContext(Context.current());
		List<CategoryWebsiteWithName> categorys = categoryEnquiryService
				.getChildCategoriesByPath(cpath, webContext);
		if (null == categorys || 0 >= categorys.size()) {
			Logger.debug("no categorys");
			return Home.notFoundResult();
		}
		LinkedHashMap<CategoryWebsiteWithName, List<ProductMessage>> data = new LinkedHashMap<CategoryWebsiteWithName, List<ProductMessage>>();
		for (CategoryWebsiteWithName categoryName : categorys) {
			Logger.debug("-------cpath="+cpath+",categoryName.cpath()="+categoryName.getCpath()+",categorys.size="+categorys.size());
			if(categorys.size()>1 && cpath.equals(categoryName.getCpath())) continue;//只有1级类目时展示1及类目，包含二级类目时不展示1及类目
			List<ProductMessage> productMessageData = Lists.newArrayList();
			Integer categoryId = categoryName.getIcategoryid();
			int i = 0;
			Map<String, String[]> queryStrings = request().queryString();
			SearchContext context = factory.fromQueryString(
					new CategorySearchCriteria(categoryId), queryStrings,
					Sets.newHashSet("popularity"));
			context.setPageSize(8);
			context.setPage(i);
			Page<String> listingsProm = searchService.searchByContext(context,
					webContext);
			List<String> listingIdsByRootId = listingsProm.getList();
			List<ProductMessage> productMessages = productMessageService
					.getProductMessages(listingIdsByRootId, webContext);
			if (null != productMessages) {
				productMessageData.addAll(productMessages);
			}
			if (productMessageData.size() > 0) {
				data.put(categoryName, productMessageData);
			}
		}

		return ok(views.html.product.category.category_product.render(cpath,
				data));
	}

	@JsonGetter
	public Result getNextPageCategoryProductData() {
		JsonNode json = request().body().asJson();
		int page = json.get("page").asInt();
		Integer categoryId = json.get("categoryId").asInt();
		int count = 0;
		Map<String, Object> resultMap = Maps.newHashMap();
		WebContext webContext = ContextUtils.getWebContext(Context.current());
		Map<String, String[]> queryStrings = request().queryString();
		SearchContext context = factory.fromQueryString(
				new CategorySearchCriteria(categoryId), queryStrings,
				Sets.newHashSet("popularity"));
		context.setPageSize(8);
		context.setPage(page + 1);
		Page<String> listingsProm = searchService.searchByContext(context,
				webContext);
		List<String> listingIdsByRootId = listingsProm.getList();
		List<ProductMessage> productMessages = productMessageService
				.getProductMessages(listingIdsByRootId, webContext);
		Html html = Html.apply("");
		if (null != productMessages && productMessages.size() > 0) {
			count = productMessages.size();
			html = views.html.product.category.category_product_part
					.render(productMessages);
		}
		resultMap.put("html", html.toString());
		resultMap.put("count", count);
		return ok(Json.toJson(resultMap));
	}

}
