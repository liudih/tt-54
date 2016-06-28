package controllers.search;

import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import play.Logger;
import play.libs.F.Promise;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http.Context;
import play.mvc.Result;
import services.base.utils.StringUtils;
import services.dodocool.base.FoundationService;
import services.dodocool.product.ProductMessageService;
import services.search.IOperatingIndex;
import services.search.ISearchContextFactory;
import services.search.ISearchService;
import services.search.criteria.KeywordSearchCriteria;
import valueobjects.base.Page;
import valueobjects.search.SearchContext;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.inject.Inject;

import context.ContextUtils;
import context.WebContext;
import dto.product.ProductMessage;

public class ProductSearch extends Controller {
	@Inject
	ISearchContextFactory factory;

	@Inject
	ISearchService searchService;

	@Inject
	ProductMessageService productMessageService;
	
	@Inject
	IOperatingIndex operatingIndex;
	
	@Inject
	FoundationService foundation;

	public Result search(String q, Integer pageNum)
			throws InstantiationException, IllegalAccessException,
			MalformedURLException, ClassNotFoundException {
		if (StringUtils.isEmpty(q)) {

		}
		SearchContext context = factory.fromQueryString(
				new KeywordSearchCriteria(q), request().queryString(),
				Sets.newHashSet("popularity"));
		WebContext webContext = ContextUtils.getWebContext(Context.current());
		context.setPageSize(16);
		Page<String> listingsProm = searchService.getProductByKeyWord(context,
				webContext);
		List<String> listingids = listingsProm.getList();
		play.Logger.debug("listingids:{}", listingids);
		List<ProductMessage> productMessages = Lists.newArrayList();
		if (null == listingids || 0 >= listingids.size()) {
			return ok(views.html.search.search_result.render(q, 0,
					productMessages));
		}
		productMessages = productMessageService.getProductMessages(listingids,
				webContext);

		return ok(views.html.search.search_result.render(q, 0, productMessages));
	}

	public Result searchMore(String q, Integer pageNum)
			throws InstantiationException, IllegalAccessException,
			MalformedURLException, ClassNotFoundException {
		WebContext webContext = ContextUtils.getWebContext(Context.current());
		SearchContext context = factory.fromQueryString(
				new KeywordSearchCriteria(q), request().queryString(),
				Sets.newHashSet("popularity"));
		context.setPage(pageNum);
		Logger.debug("pageNum:{}", pageNum);
		context.setPageSize(16);
		Page<String> listingsProm = searchService.getProductByKeyWord(context,
				webContext);
		Map<String, Object> m = new HashMap<String, Object>();
		m.put("page", pageNum);
		List<String> listingids = listingsProm.getList();
		if (null == listingids || 0 >= listingids.size()) {
			m.put("size", 0);
			return ok(Json.toJson(m));
		}
		List<ProductMessage> productMessages = productMessageService
				.getProductMessages(listingids, webContext);
		m.put("html", views.html.search.search_more.render(productMessages)
				.toString());
		m.put("size", productMessages.size());

		return ok(Json.toJson(m));
	}
	
	public Promise<Result> indexing(boolean drop, boolean create) {
		final int siteID = foundation.getSiteID();
		Logger.debug("SiteID:-------------{}",siteID);
		return Promise.promise(() -> {
			operatingIndex.indexAll(drop, create, siteID);
			return ok("Indexing Done");
		});
	}
	
	public Promise<Result> deleteIndex() {
		final int siteID = foundation.getSiteID();
		return Promise.promise(() -> {
			operatingIndex.deleteAll(siteID);
			return ok("Delete Index Done");
		});
	}
}
