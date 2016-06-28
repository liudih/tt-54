package controllers.search;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;

import play.Logger;
import play.libs.F.Promise;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Http.Context;
import play.mvc.Result;
import play.mvc.Results;
import services.base.CurrencyService;
import services.base.FoundationService;
import services.product.CategoryEnquiryService;
import services.product.IProductBadgeService;
import services.product.ProductBadgeService;
import services.product.ProductEnquiryService;
import services.search.IAsyncSearchService;
import services.search.IndexingService;
import services.search.KeywordSuggestService;
import services.search.SearchContextFactory;
import services.search.SearchService;
import services.search.criteria.CategorySearchCriteria;
import services.search.criteria.KeywordSearchCriteria;
import valueobjects.base.Page;
import valueobjects.price.Price;
import valueobjects.product.ProductBadge;
import valueobjects.product.ProductIndexingContextExtra;
import valueobjects.product.category.CategoryReverseComposite;
import valueobjects.product.index.RecommendDoc;
import valueobjects.product.spec.SingleProductSpec;
import valueobjects.search.ProductIndexingContext;
import valueobjects.search.SearchContext;
import valueobjects.search.SearchPage;
import valueobjects.search.agg.CategorySearchAggValue;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.common.eventbus.EventBus;

import dto.product.CategoryName;
import dto.product.CategoryWebsiteWithName;
import events.search.KeywordSearchEvent;

public class ProductSearch extends Controller {

	@Inject
	FoundationService foundation;

	@Inject
	KeywordSuggestService suggestService;

	@Inject
	IndexingService indexingService;

	@Inject
	IAsyncSearchService genericSearch;

	@Inject
	SearchContextFactory factory;

	@Inject
	CategoryEnquiryService categoryEnquiryService;

	@Inject
	ProductBadgeService badgeService;

	@Inject
	EventBus eventBus;

	@Inject
	ProductEnquiryService productEnquiryService;
	@Inject
	CurrencyService currencyService;

	@Inject
	SearchService searchService;

	public Result suggest(String q) {
		return ok(Json.toJson(suggestService.getSuggestions(q,
				foundation.getSiteID(), foundation.getLanguage())));
	}

	public Promise<Result> searchOld(String q, Integer st) {
		return search(q, st, null);
	}

	public Promise<Result> search(String q, Integer st, String path) {
		if (StringUtils.isEmpty(q)) {
			return Promise.pure(redirect(controllers.base.routes.Home.home()));
		}
		Logger.info("keyword:" + q + " st:" + st + " path:" + path);
		System.out.println("/n");
		List<Integer> categoryIdList = Lists.newArrayList();
		final int siteID = foundation.getSiteID();
		final int language = foundation.getLanguage();
		CategoryReverseComposite rev = null;
		if (StringUtils.isNotEmpty(path)) {

			try {
				path = URLDecoder.decode(path, "UTF-8");
				CategoryName cn = categoryEnquiryService.getCategoryNameByPath(
						path, language, siteID);

				rev = categoryEnquiryService.getReverseCategory(
						cn.getIcategoryid(), language, siteID);
				Logger.info("rev:" + rev);

				if (rev.getSelf().getIlevel() == 3) {
					FluentIterable.from(rev.getParent().getChildren()).forEach(
							item -> categoryIdList.add(item.getSelf()
									.getIcategoryid()));// 获取同级
				}
				categoryIdList.add(rev.getSelf().getIcategoryid());// 先统计自己总数
				if (rev != null
						&& CollectionUtils.isNotEmpty(rev.getChildren())) {
					FluentIterable.from(rev.getChildren()).forEach(
							item -> categoryIdList.add(item.getSelf()
									.getIcategoryid()));// 获取子品类
				}
			} catch (Exception e) {
				Logger.error("getCategoryList error " + e.getMessage());
			}
		}
		// ------------------搜索条件需要的品类：categoryIdList; q;

		SearchContext context = factory.fromQueryString(
				new KeywordSearchCriteria(q), request().queryString(),
				Sets.newHashSet("popularity"));

		if (path != null) {
			String ckey = path;
			try {
				ckey = URLDecoder.decode(path, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
				ckey = path;
			}
			// --- meta preparation
			CategoryName cn = categoryEnquiryService.getCategoryNameByPath(
					ckey, language, siteID);
			context.getCriteria().add(
					new CategorySearchCriteria(cn.getIcategoryid()));
		}

		final String remoteAddress = request().remoteAddress();
		final String ltc = foundation.getLoginContext().getLTC();
		final String stc = foundation.getLoginContext().getSTC();
		final String ccy = foundation.getCurrency();

		Promise<Page<String>> listingsProm = genericSearch.asyncSearch(context,
				siteID, language);

		final String cpath = path;
		final CategoryReverseComposite categoryReverseComposite = rev;
		return listingsProm.map(listings -> {
			// log search keyword only if it is the first page
				if (context.getPage() == 0) {
					eventBus.post(new KeywordSearchEvent(q, listings, siteID,
							language, remoteAddress, ltc, stc));
				}

				List<ProductBadge> badges = badgeService
						.getNewProductBadgesByListingIds(listings.getList(),
								language, siteID, ccy, null);
				Map<String, ProductBadge> badgeMap = Maps.newHashMap();
				for (ProductBadge b : badges) {
					badgeMap.put(b.getListingId(), b);
				}
				Page<ProductBadge> pagedResult = listings.map(id -> badgeMap
						.get(id));

				/**
				 * get cagegoryID count
				 */
				SearchPage<String> agg = (SearchPage<String>) listings;

				CategorySearchAggValue aggs = (CategorySearchAggValue) agg
						.getiSearchAggValue();

				Map<Integer, Long> curreryCatogeryCount = aggs
						.getCategoryCounts();
				Set<CategoryReverseComposite> nodes = Sets.newHashSet();
				getCategoryList(cpath, categoryReverseComposite, language,
						siteID, curreryCatogeryCount, nodes);

				return ok(views.html.search.search_result.render(q,
						pagedResult, context, st, request().queryString(),
						curreryCatogeryCount, Lists.newArrayList(nodes)));
			});
	}

	private void getCategoryList(String path, CategoryReverseComposite rev,
			int language, int siteID, Map<Integer, Long> curreryCatogeryCount,
			Set<CategoryReverseComposite> nodes) {

		try {

			// 获取所以产品的listingId
			if (MapUtils.isNotEmpty(curreryCatogeryCount)) {// 搜索结果返回数据
				List<Integer> categaoryLists = Lists.newArrayList();
				Iterator<Integer> countItem = curreryCatogeryCount.keySet()
						.iterator();
				Logger.debug("curreryCatogeryCount:" + curreryCatogeryCount);
				while (countItem.hasNext()) {
					categaoryLists.add(countItem.next());
				}
				List<Integer> parentCategoryIds = Lists.newArrayList();

				// 如果是默认查全部产品ROOT categoryId
				if (StringUtils.isEmpty(path)) {
					if (CollectionUtils.isNotEmpty(categaoryLists)) {
						// 第一级
						FluentIterable
								.from(categaoryLists)
								.forEach(
										item -> {
											CategoryWebsiteWithName categoryWebsiteWithName = categoryEnquiryService
													.getCategoryForLevelOne(
															item, language,
															siteID);
											if (categoryWebsiteWithName != null
													&& !parentCategoryIds
															.contains(categoryWebsiteWithName
																	.getIcategoryid())) {
												nodes.add(new CategoryReverseComposite(
														categoryWebsiteWithName,
														null, null));
												parentCategoryIds
														.add(categoryWebsiteWithName
																.getIcategoryid());
											}
										});
					}
				} else {
					nodes.add(rev);
				}
			}
		} catch (Exception e) {
			Logger.error("getCategoryList error:" + e.getMessage());
		}
	}

	public Promise<Result> indexing(boolean drop, boolean create) {
		final int siteID = foundation.getSiteID();
		return Promise.promise(() -> {
			indexingService.indexAll(drop, create, siteID);
			return ok("Indexing Done");
		});
	}

	public Promise<Result> deleteIndex() {
		final int siteID = foundation.getSiteID();
		return Promise.promise(() -> {
			indexingService.deleteAll(siteID);
			return ok("Delete Index Done");
		});
	}

	public Promise<Result> createIndexbyListing(String type, String value) {
		final int siteID = foundation.getSiteID();
		return Promise.promise(() -> {
			boolean result = true;
			if ("sku".equals(type)) {
				Logger.info("controller sku->{}--site-->{}", value, siteID);
				String listingId = productEnquiryService.getListingsBySku(
						value, siteID);
				result = indexingService.index_new(listingId);
			}
			if ("listing".equals(type)) {
				Logger.info("Indexing Single Listing controller: {}", value);
				result = indexingService.index_new(value);
			}
			if (result) {
				return ok("create Index Done");
			} else {
				return ok("failure");
			}
		});
	}

	@BodyParser.Of(BodyParser.Json.class)
	public Result currencyConversion() {
		try {
			Map<String, Object> resultMap = Maps.newHashMap();
			JsonNode json = request().body().asJson();
			if (json.get("ccode") != null) {
				String currencyCode = json.get("ccode").asText();
				Double lower = json.get("lowerPrice").asDouble();
				Double higher = json.get("higherPrice").asDouble();
				Double lowerPrice = currencyService.exchange(lower, "USD",
						currencyCode);
				Double higherPrice = currencyService.exchange(higher, "USD",
						currencyCode);
				resultMap.put("lowerPrice", lowerPrice);
				resultMap.put("higherPrice", higherPrice);
				return ok(Json.toJson(resultMap));
			}
		} catch (Exception ex) {
			Logger.debug("currencyConversion error: ", ex);
			return play.mvc.Results.internalServerError(ex.getMessage());
		}
		return play.mvc.Results.badRequest();
	}

	public Promise<Result> deleteIndexbyListing(String listingid) {
		return Promise.promise(() -> {
			indexingService.deleteByListing(listingid);
			return ok("delete Index Done");
		});
	}

	public Promise<Result> indexingBeginDate(Integer siteId, String beginDate,
			String endDate) {
		return Promise
				.promise(() -> {
					try {
						SimpleDateFormat formatter = new SimpleDateFormat(
								"yyyyMMddHHmmss");
						Date bdate = formatter.parse(beginDate);
						Date edate = formatter.parse(endDate);
						List<com.website.dto.product.Product> plist = productEnquiryService
								.getProducts(siteId, bdate, edate);
						List<String> listingids = Lists.transform(plist,
								p -> p.getListingId());
						indexingService.batchIndex(listingids, siteId);
						return ok("create Index Done " + listingids.size());
					} catch (Exception ex) {
						Logger.error("create Index Error: ", ex);
						return Results.internalServerError(
								"create Index Error: {}", ex.getMessage());
					}
				});
	}

	@BodyParser.Of(BodyParser.Json.class)
	public Promise<Result> createIndex() {
		return Promise
				.promise(() -> {
					try {
						boolean result = true;
						JsonNode json = request().body().asJson();
						if (json == null) {
							return internalServerError("invalid json!");
						}
						ObjectMapper map = new ObjectMapper();
						Logger.debug(json.toString());
						ProductIndexingContextExtra productIndexingContextExtra = map
								.readValue(json.toString(),
										ProductIndexingContextExtra.class);
						SingleProductSpec spec = new SingleProductSpec(
								productIndexingContextExtra.getPrice()
										.getSpec().getListingID(),
								productIndexingContextExtra.getPrice()
										.getSpec().getQty());
						Price p = new Price(spec);
						p.setCurrency(productIndexingContextExtra.getPrice()
								.getCurrency());
						p.setDiscount(productIndexingContextExtra.getPrice()
								.getDiscount());
						p.setLossAllowed(productIndexingContextExtra.getPrice()
								.isLossAllowed());
						p.setRate(productIndexingContextExtra.getPrice()
								.getRate());
						p.setSymbol(productIndexingContextExtra.getPrice()
								.getSymbol());
						p.setUnitBasePrice(productIndexingContextExtra
								.getPrice().getUnitBasePrice());
						p.setUnitCost(productIndexingContextExtra.getPrice()
								.getUnitCost());
						p.setUnitPrice(productIndexingContextExtra.getPrice()
								.getUnitPrice());
						p.setValidFrom(productIndexingContextExtra.getPrice()
								.getValidFrom());
						p.setValidTo(productIndexingContextExtra.getPrice()
								.getValidTo());

						List<RecommendDoc> crlist = Lists.newArrayList();
						if (productIndexingContextExtra.getCategoryRecommend() != null
								&& productIndexingContextExtra
										.getCategoryRecommend().size() > 0) {
							crlist = Lists.transform(
									productIndexingContextExtra
											.getCategoryRecommend(),
									p1 -> {
										return new RecommendDoc(p1
												.getCategoryId(), p1
												.getSequence(), p1.getSiteid(),
												p1.getDevice());
									});

						}

						ProductIndexingContext productIndexingContext = new ProductIndexingContext(
								productIndexingContextExtra.getSiteId(),
								productIndexingContextExtra.getListingId(),
								productIndexingContextExtra.getProduct(),
								productIndexingContextExtra.getCategories(),
								productIndexingContextExtra.getOtherInfos(),
								productIndexingContextExtra.getAttributes(),
								productIndexingContextExtra.getSales(),
								productIndexingContextExtra.getTags(), p,
								productIndexingContextExtra.getViewCount(),
								productIndexingContextExtra.getRelatedSku(),
								productIndexingContextExtra.getStorage(),
								crlist);
						Logger.info("controller json --listingid-->{}",
								productIndexingContextExtra.getListingId());
						result = indexingService
								.indexJson(productIndexingContext);
						if (result) {
							return ok("{\"result\":true}");
						} else {
							return ok("{\"result\":false}");
						}
					} catch (Exception ex) {
						Logger.error("--createIndex--json-", ex);
						return internalServerError(ex.getMessage());
					}
				});

	}
}
