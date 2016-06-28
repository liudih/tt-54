package controllers.product;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.elasticsearch.common.collect.FluentIterable;

import play.libs.F;
import play.mvc.Controller;
import play.mvc.Result;
import services.base.FoundationService;
import services.base.StorageService;
import services.base.utils.MetaUtils;
import services.product.CategoryEnquiryService;
import services.product.ProductAdvertisingCompositeEnquiry;
import services.product.ProductBadgeService;
import services.product.ProductEnquiryService;
import services.search.SearchContextFactory;
import services.search.SearchServiceWithHttpContext;
import services.search.criteria.CategorySearchCriteria;
import services.search.sort.RecommendSortOrder;
import services.search.sort.SaleCountSortOrder;
import services.search.sort.ViewCountSortOrder;
import valueobjects.base.Page;
import valueobjects.product.AdItem;
import valueobjects.product.ProductAdertisingContext;
import valueobjects.product.ProductBadge;
import valueobjects.product.category.CategoryReverseComposite;
import valueobjects.search.SearchContext;
import valueobjects.search.SearchPage;
import valueobjects.search.agg.CategorySearchAggValue;

import com.google.common.base.Function;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimaps;
import com.google.inject.Inject;

import controllers.base.Home;
import dto.Category;
import dto.Storage;
import dto.product.CategoryName;
import dto.product.CategoryWebsiteWithName;

public class CategoryProduct extends Controller {

	@Inject
	ProductEnquiryService productEnquiryService;

	@Inject
	FoundationService foundationService;

	@Inject
	CategoryEnquiryService categoryEnquiryService;

	@Inject
	SearchServiceWithHttpContext genericSearch;

	@Inject
	SearchContextFactory searchFactory;

	@Inject
	ProductBadgeService badgeService;

	@Inject
	ProductAdvertisingCompositeEnquiry productAdvertisingService;

	@Inject
	StorageService storageService;

	public Result showCategoryProduct(final String cpath, final Integer page,
			final Integer size, final String filter, Integer st) {

		int lang = foundationService.getLanguage();
		int site = foundationService.getSiteID();
		String currency = foundationService.getCurrency();
		String device = foundationService.getDevice();
		String ckey = cpath;
		try {
			ckey = URLDecoder.decode(cpath, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			ckey = cpath;
		}
		// --- meta preparation
		CategoryName cn = categoryEnquiryService.getCategoryNameByPath(ckey,
				lang, site);
		if (cn == null) {
			return Home.notFoundResult();
		}
		MetaUtils.currentMetaBuilder().setTitle(cn.getCtitle())
				.setDescription(cn.getCdescription())
				.addKeyword(cn.getCkeywords());

		Map<String, String[]> queryStrings = request().queryString();
		CategoryReverseComposite rev = categoryEnquiryService
				.getReverseCategory(cn.getIcategoryid(), lang, site);

		SearchContext context = searchFactory.fromQueryString(
				new CategorySearchCriteria(cn.getIcategoryid()), queryStrings,
				null);
		if (!queryStrings.containsKey("price")
				&& !queryStrings.containsKey("sale")
				&& !queryStrings.containsKey("review")
				&& !queryStrings.containsKey("discount")) {
			context.getSort().add(new RecommendSortOrder(true, cn.getIcategoryid(), 
					site, device));
		}
		// Use search service!
		Page<String> listingIDs = genericSearch.search(context, site, lang);
		List<ProductBadge> badges = badgeService
				.getNewProductBadgesByListingIds(listingIDs.getList(), lang,
						site, currency, null);
		Map<String, ProductBadge> badgeMap = Maps.newHashMap();
		for (ProductBadge b : badges) {
			badgeMap.put(b.getListingId(), b);
		}
		Page<ProductBadge> badgePage = listingIDs.map(id -> badgeMap.get(id));

		// ----search attribute
		// CategoryAttributeMap attributeMap = categoryEnquiryService
		// .getCategoryAttributeMapByPath(cpath);

		// 获取广告
		ProductAdertisingContext pac = new ProductAdertisingContext(cn
				.getIcategoryid().toString(), 2, site, lang, 3, device);

		List<AdItem> advertisingList = productAdvertisingService
				.getAdvertisings(pac);

		// hot列表
		List<ProductBadge> hotlist = this.getHotProducts(cn.getIcategoryid());

		List<Storage> storageList = storageService.getAllStorages();

		/**
		 * get cagegoryID count
		 */
		SearchPage<String> agg = (SearchPage<String>) listingIDs;

		CategorySearchAggValue aggs = (CategorySearchAggValue) agg
				.getiSearchAggValue();

		Map<Integer, Long> curreryCatogeryCount = aggs.getCategoryCounts();

		return ok(views.html.product.category_product2.render(badgePage, rev,
				st, queryStrings, advertisingList, storageList, hotlist,
				context, curreryCatogeryCount));
	}

	public F.Tuple<List<Category>, ListMultimap<Integer, Category>> getFirstSecondLevelCategories() {
		List<CategoryWebsiteWithName> oneLevelCategories = categoryEnquiryService
				.getCategoriesByLevel(foundationService.getLanguage(),
						foundationService.getSiteID(), 1);

		List<Category> oneCategoryList = Lists.transform(oneLevelCategories,
				new Function<CategoryWebsiteWithName, dto.Category>() {
					@Override
					public dto.Category apply(CategoryWebsiteWithName self) {
						return new dto.Category(self.getIid(), self
								.getIparentid(), self.getCname(), self
								.getCpath());
					}
				});

		List<CategoryWebsiteWithName> twoLevelCategories = categoryEnquiryService
				.getCategoriesByLevel(foundationService.getLanguage(),
						foundationService.getSiteID(), 2);

		List<Category> twoCategoryList = Lists.transform(twoLevelCategories,
				new Function<CategoryWebsiteWithName, Category>() {
					@Override
					public Category apply(CategoryWebsiteWithName self) {
						return new Category(self.getIid(), self.getIparentid(),
								self.getCname(), self.getCpath());
					}
				});

		ListMultimap<Integer, dto.Category> categoryMap = Multimaps.index(
				twoCategoryList, c -> c.getIparentid());
		return F.Tuple(oneCategoryList, categoryMap);
	}

	public Result getAllCategories() {
		F.Tuple<List<Category>, ListMultimap<Integer, Category>> tuple = getFirstSecondLevelCategories();
		return ok(views.html.product.category.allcategories.render(tuple._1,
				tuple._2.asMap()));
	}

	private List<ProductBadge> getHotProducts(Integer categoryid) {
		int lang = foundationService.getLanguage();
		int siteid = foundationService.getSiteID();
		String currency = foundationService.getCurrency();
		SearchContext context = searchFactory
				.pureSearch(new CategorySearchCriteria(categoryid));
		context.getSort().add(new SaleCountSortOrder(false));
		context.getSort().add(new ViewCountSortOrder(false));
		context.setPageSize(6);
		context.setPage(0);
		Page<String> listingids = genericSearch.search(context, siteid, lang);
		Page<ProductBadge> badgePage = listingids.batchMap(list -> badgeService
				.getProductBadgesByListingIDs(list, lang, siteid, currency,
						null));
		return badgePage.getList();
	}
}
