package controllers.search;

import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import play.Logger;
import play.libs.F.Promise;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Http.Context;
import play.mvc.Result;
import services.ICurrencyService;
import services.IEventBusService;
import services.base.FoundationService;
import services.base.utils.StringUtils;
import services.product.IProductBadgeService;
import services.product.IProductEnquiryService;
import services.product.ProductMessageService;
import services.product.ProductUtilService;
import services.search.IAsyncSearchService;
import services.search.IKeyWordSuggestService;
import services.search.ISearchContextFactory;
import services.search.ISearchService;
import services.search.criteria.KeywordSearchCriteria;
import valueobjects.base.Page;
import valueobjects.product.ProductBadge;
import valueobjects.search.SearchContext;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.common.eventbus.EventBus;

import context.ContextUtils;
import context.WebContext;
import dto.product.ProductMessage;
import events.search.KeywordSearchEvent;

public class ProductSearch extends Controller {

	@Inject
	FoundationService foundation;

	@Inject
	IKeyWordSuggestService suggestService;

	@Inject
	IAsyncSearchService genericSearch;

	@Inject
	ISearchContextFactory factory;

	@Inject
	IProductBadgeService badgeService;

	@Inject
	IProductEnquiryService productEnquiryService;

	@Inject
	ICurrencyService currencyService;

	@Inject
	ISearchService searchService;

	@Inject
	ProductMessageService productMessageService;

	@Inject
	ProductUtilService productUtilService;

	@Inject
	IEventBusService eventBusService;

	public Result suggest(String q) {
		int lang = foundation.getLanguage();
		int site = foundation.getSiteID();
		return ok(Json.toJson(suggestService.getSuggestions(q, site, lang)));
	}

	public Result search(String q, Integer st) {
		SearchContext context = factory.fromQueryString(
				new KeywordSearchCriteria(q), request().queryString(),
				Sets.newHashSet("popularity"));

		final String remoteAddress = request().remoteAddress();
		int language = foundation.getLanguage();
		int siteID = foundation.getSiteID();
		String currency = foundation.getCurrency();

		final String ltc = foundation.getLoginContext().getLTC();
		final String stc = foundation.getLoginContext().getSTC();
		final String ccy = foundation.getCurrency();

		context.setPageSize(12);
		Page<String> listingsProm = genericSearch.getasynSearch(context,
				foundation.getWebContext());

		Page<ProductBadge> pagedResult = listingsProm
				.batchMap(list -> badgeService.getProductBadgesByListingIDs(
						list, language, siteID, ccy, null, false, false));

		productUtilService.addProductBadgeCollect(pagedResult.getList(),
				listingsProm.getList(), language, siteID, currency);

		if (context.getPage() == 0) {
			eventBusService.post(new KeywordSearchEvent(q, listingsProm,
					siteID, language, remoteAddress, ltc, stc));
		}

		return ok(views.html.search.search_result.render(q, pagedResult,
				context, st));
	}

	@BodyParser.Of(BodyParser.Json.class)
	public Result currencyConversion() {
		Map<String, Object> resultMap = Maps.newHashMap();
		JsonNode json = request().body().asJson();
		String currencyCode = json.get("ccode").asText();
		Double lower = json.get("lowerPrice").asDouble();
		Double higher = json.get("higherPrice").asDouble();
		Double lowerPrice = currencyService
				.exchange(lower, "USD", currencyCode);
		Double higherPrice = currencyService.exchange(higher, "USD",
				currencyCode);
		resultMap.put("lowerPrice", lowerPrice);
		resultMap.put("higherPrice", higherPrice);
		return ok(Json.toJson(resultMap));
	}

	public Result searchNavigation() {
		return ok(views.html.search.search.render());
	}

	public Result searchMore(String q, Integer pageNum)
			throws InstantiationException, IllegalAccessException,
			MalformedURLException, ClassNotFoundException {
		int language = foundation.getLanguage();
		int siteID = foundation.getSiteID();
		String currency = foundation.getCurrency();
		final String ccy = foundation.getCurrency();
		SearchContext context = factory.fromQueryString(
				new KeywordSearchCriteria(q), request().queryString(),
				Sets.newHashSet("popularity"));
		context.setPage(pageNum);
		Logger.debug("pageNum:{}", pageNum);
		context.setPageSize(12);
		
		Page<String> listingsProm = genericSearch.getasynSearch(context,
				foundation.getWebContext());
		Map<String, Object> m = new HashMap<String, Object>();
		m.put("page", pageNum);

		Page<ProductBadge> pagedResult = listingsProm
				.batchMap(list -> badgeService.getProductBadgesByListingIDs(
						list, language, siteID, ccy, null, false, true));
		List<String> listingids = listingsProm.getList();

		productUtilService.addProductBadgeCollect(pagedResult.getList(),
				listingsProm.getList(), language, siteID, currency);

		if (null == listingids || 0 >= listingids.size()) {
			m.put("size", 0);
			return ok(Json.toJson(m));
		}

		m.put("html", views.html.search.search_more.render(pagedResult)
				.toString());
		m.put("totlePage", listingsProm.totalPages());
		m.put("size", pagedResult.getList().size());

		return ok(Json.toJson(m));
	}
}
