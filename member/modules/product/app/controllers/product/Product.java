package controllers.product;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import mapper.product.ProductBaseMapper;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.common.collect.Maps;

import play.Logger;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Http.Context;
import play.mvc.Result;
import play.twirl.api.Html;
import services.IStorageService;
import services.base.FoundationService;
import services.base.StorageParentService;
import services.base.SystemParameterService;
import services.base.utils.DateFormatUtils;
import services.base.utils.JsonFormatUtils;
import services.base.utils.MetaUtils;
import services.product.CategoryEnquiryService;
import services.product.CategoryLabelBaseService;
import services.product.IProductBadgeService;
import services.product.IProductUpdateService;
import services.product.ProductAdvertisingCompositeEnquiry;
import services.product.ProductBadgeService;
import services.product.ProductCompositeEnquiry;
import services.product.ProductCompositeRenderer;
import services.product.ProductContextUtils;
import services.product.ProductEnquiryService;
import services.product.ProductInterceptUrlService;
import services.product.ProductLabelService;
import services.product.ProductMultiatributeService;
import services.product.ProductUrlService;
import services.search.IDailyDealEnquiryService;
import services.search.SearchContextFactory;
import services.search.SearchServiceWithHttpContext;
import services.search.criteria.CategorySearchCriteria;
import services.search.criteria.DisCountCriteria;
import services.search.criteria.ProductLabelType;
import services.search.criteria.ProductTagsCriteria;
import services.search.criteria.PublishDateCriteria;
import services.search.criteria.StorageIdSearchCriteria;
import services.search.filter.PriceRangeFilter;
import services.search.filter.StorageFilter;
import services.search.sort.DiscountSortOrder;
import services.search.sort.SaleCountSortOrder;
import services.search.sort.ViewCountSortOrder;
import valueobjects.base.Page;
import valueobjects.product.AdItem;
import valueobjects.product.CategoryLabelBase;
import valueobjects.product.ProductAdertisingContext;
import valueobjects.product.ProductBadge;
import valueobjects.product.ProductComposite;
import valueobjects.product.ProductContext;
import valueobjects.product.ProductNewarrivalsCalculateItem;
import valueobjects.product.category.CategoryClearance;
import valueobjects.product.category.CategoryReverseComposite;
import valueobjects.search.ISearchCriteria;
import valueobjects.search.ISearchFilter;
import valueobjects.search.SearchContext;
import valueobjects.search.SearchPage;
import valueobjects.search.agg.CategorySearchAggValue;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.google.common.collect.BoundType;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;
import com.google.common.collect.Range;
import com.google.common.collect.Sets;
import com.google.common.eventbus.EventBus;

import controllers.base.Home;
import dto.Storage;
import dto.StorageParent;
import dto.product.CategoryName;
import dto.product.CategoryWebsiteWithName;
import dto.product.DailyDeal;
import dto.product.ProductInterceptUrl;
import dto.product.ProductMultiattributeEntity;
import dto.product.ProductParentUrl;
import dto.product.ProductUrl;
import events.product.ProductViewEvent;
import extensions.product.subscribe.ISubscribeProvider;

public class Product extends Controller {

	@Inject
	ProductCompositeEnquiry compositeEnquiry;

	@Inject
	ProductCompositeRenderer compositeRenderer;

	@Inject
	ProductContextUtils contextUtils;

	@Inject
	FoundationService foundation;

	@Inject
	ProductEnquiryService productEnquiryService;

	@Inject
	EventBus eventBus;

	@Inject
	ProductBaseMapper productBaseMapper;

	@Inject
	ProductMultiatributeService productMultiatributeService;

	@Inject
	SearchServiceWithHttpContext genericSearch;

	@Inject
	SearchContextFactory searchFactory;

	@Inject
	ProductBadgeService badgeService;

	@Inject
	CategoryEnquiryService categoryEnquiryService;

	@Inject
	ProductAdvertisingCompositeEnquiry productAdvertisingService;

	@Inject
	Set<ISubscribeProvider> subscribeProviders;

	@Inject
	SystemParameterService parameterService;

	@Inject
	CategoryLabelBaseService categoryLabelBaseService;

	@Inject
	IProductUpdateService productUpdateService;

	@Inject
	ProductLabelService productLabelService;

	@Inject
	IDailyDealEnquiryService dailyDealEnquiryService;

	@Inject
	ProductUrlService productUrlService;

	@Inject
	ProductInterceptUrlService productInterceptUrlService;

	@Inject
	IStorageService storageService;
	
	@Inject
	StorageParentService storageParentService;

	final boolean bhot = true;

	final String ZERO_DATE_STR = " 00:00:00";

	final String DAY_MAX_DATE_STR = " 23:59:59";

	public Result view(final String title) {
		/*
		 * 1.根据title（url)查找 parenturl,如果没找到表示不是多属性产品，直接往下走 2.如果找到 需要找到主产品的url
		 */
		String ctitle = title;
		ProductParentUrl productParentUrl = this.productEnquiryService
				.getProductParentUrlByUrl(ctitle, foundation.getLanguage());
		if (null != productParentUrl) {
			String cparentsku = productParentUrl.getCparentsku();
			String listingId = this.productEnquiryService
					.getListingIdByParentSkuAndWebsiteIdAndStatusAndIsMain(
							cparentsku, 1, foundation.getSiteID(), true);
			if (null == listingId) {
				return Home.notFoundResult();
			} else {
				ProductInterceptUrl interceptUrl = this.productInterceptUrlService
						.getUrlByLanuageidAndListingid(
								foundation.getLanguage(), listingId);
				if (null != interceptUrl) {
					ctitle = interceptUrl.getCurl();
				} else {
					ProductUrl productUrl = this.productUrlService
							.getProductUrlsByListingId(listingId,
									foundation.getLanguage());
					ctitle = productUrl.getCurl();
				}
			}
		}
		boolean iscampaign = false;
		Http.Context ctx = Context.current();
		String id = ctx.request().getQueryString("campaignid");
		if (id != null) {
			iscampaign = true;
		}

		// title -> listingId -> Sku -> lang
		ProductContext context = contextUtils.createProductContext(ctitle,
				foundation.getWebContext(), iscampaign);
		if (null == context) {
			return Home.notFoundResult();
		}

		ProductComposite vo = compositeEnquiry.getProductComposite(context);

		// meta handling
		MetaUtils
				.currentMetaBuilder()
				.setTitle((String) vo.getAttributes().get("meta-title"))
				.setDescription(
						(String) vo.getAttributes().get("meta-description"))
				.addKeyword((String) vo.getAttributes().get("meta-keywords"));

		eventBus.post(new ProductViewEvent(foundation.getLoginContext(),
				context));

		return ok(views.html.product.detail.render(vo, compositeRenderer));
	}

	/**
	 * @param clistingid
	 *            被绑定商品listingid
	 * @param categoryid
	 *            主商品listingid
	 * @return Result
	 * @Title: getMultiatributeProduct
	 * @Description: TODO(展示绑定商品属性)
	 * @author Lij
	 */
	@JsonGetter
	public Result getMultiatributeProduct(String clistingid,
			String mainclistingid, String type) {
		// int siteID = foundation.getSiteID();
		Map<String, List<ProductMultiattributeEntity>> productMultiatribute = productMultiatributeService
				.getProductMultiatribute(clistingid, mainclistingid, type);
		String jsontxt = JsonFormatUtils.beanToJson(productMultiatribute);
		return ok(jsontxt);
	}

	public Result gadget(int type, Integer page, Integer size, Integer st) {
		MetaUtils.currentMetaBuilder().setTitle("0.99");
		int lang = foundation.getLanguage();
		int site = foundation.getSiteID();
		String currency = foundation.getCurrency();

		List<Double> dr;
		switch (type) {
		case 0:
			dr = Lists.newArrayList(0d, 0.995d);
			break;
		case 1:
			dr = Lists.newArrayList(0.995d, 1.995d);
			break;
		case 2:
			dr = Lists.newArrayList(1.995d, 2.995d);
			break;
		case 3:
			dr = Lists.newArrayList(2.995d, 3.995d);
			break;
		case 4:
			dr = Lists.newArrayList(3.995d, 4.995d);
			break;
		default:
			dr = Lists.newArrayList(0d, 0.995d);
			break;
		}
		Range<Double> priceRange = Range.range(dr.get(0), BoundType.CLOSED,
				dr.get(1), BoundType.OPEN);
		PriceRangeFilter c = new PriceRangeFilter(priceRange);
		SearchContext context = searchFactory.fromQueryCurrentHttpContext(null,
				request().queryString());
		context.getFilter().add(c);
		Logger.debug("PriceRange {}", c);
		Page<String> listingids = genericSearch.search(context, site, lang);
		Page<ProductBadge> badgePage = listingids.batchMap(list -> badgeService
				.getProductBadgesByListingIDs(list, lang, site, currency, null,
						true, true));

		// 获取有产品的类目
		List<CategoryWebsiteWithName> catelist = categoryEnquiryService
				.getProductCatelist(null, Lists.newArrayList(c), site, lang);

		// 获取广告
		ProductAdertisingContext pac = new ProductAdertisingContext(null, 4,
				foundation.getSiteID(), foundation.getLanguage(), 3,
				foundation.getDevice());
		List<AdItem> advertisingList = productAdvertisingService
				.getAdvertisings(pac);

		return ok(views.html.product.gadget.gadget.render(badgePage, catelist,
				advertisingList, type, context, st));

	}

	public Result newarrivals(int type, Integer page, Integer size, Integer st,
			Integer iscate) throws ParseException {
		MetaUtils.currentMetaBuilder().setTitle("New Arrivals");
		// 获取广告
		ProductAdertisingContext pac = new ProductAdertisingContext(null, 3,
				foundation.getSiteID(), foundation.getLanguage(), 3,
				foundation.getDevice());

		List<AdItem> advertisingList = productAdvertisingService
				.getAdvertisings(pac);

		List<ProductNewarrivalsCalculateItem> pciList = Lists.newArrayList();

		String newValidDays = parameterService.getSystemParameter(
				foundation.getSiteID(), foundation.getLanguage(),
				"NewarrivalsValidDays");
		String newStatDays = parameterService.getSystemParameter(
				foundation.getSiteID(), foundation.getLanguage(),
				"NewarrivalsStatDays");

		// 现在统计是 前12天， 动态从数据库取出数据
		Date statDate = DateFormatUtils.getNowNotHmsBeforeByDay(
				Calendar.DAY_OF_WEEK, -Integer.parseInt(newStatDays));

		// 动态获取新品有效时间， 现在默认取出数据库存取的 30 天
		// Date newValidDate = DateFormatUtils.getNowNotHmsBeforeByDay(
		// Calendar.DAY_OF_WEEK, Integer.parseInt(newValidDays));

		// 根据时间统计新品数据
		List<ProductNewarrivalsCalculateItem> newarrivalsList = productEnquiryService
				.findAllNewarrivalsGroupByCreateDate(statDate,
						Integer.parseInt(newValidDays));

		this.getNewarrivalsByDate(pciList, newarrivalsList,
				Integer.parseInt(newValidDays));

		// 新品类别
		int lang = foundation.getLanguage();
		int site = foundation.getSiteID();
		String currency = foundation.getCurrency();

		PublishDateCriteria c = null;
		String publishDate = null;
		String publishDateType = null;

		// 需要判断是点击单个日期 ，还是下面的一个月之前 ，还是两个星期之前，组装查询PublishDateCriteria
		Map<String, String[]> queryStrings = new HashMap<String, String[]>();
		queryStrings.putAll(request().queryString());
		if (queryStrings.containsKey("statPublishDate")
				&& queryStrings.containsKey("statPublishDateType")
				&& queryStrings.get("statPublishDate")[0] != null
				&& !"".equals(queryStrings.get("statPublishDate")[0])) {

			String[] publishDateArry = queryStrings.get("statPublishDate");
			publishDate = publishDateArry[0];

			String[] publishDateTypeArry = queryStrings
					.get("statPublishDateType");
			publishDateType = publishDateTypeArry[0];
			if ("1".equals(publishDateType)) {
				c = new PublishDateCriteria(Range.closed(
						DateFormatUtils.getFormatDateYmdhmsByStr(publishDate
								+ this.ZERO_DATE_STR),
						DateFormatUtils.getFormatDateYmdhmsByStr(publishDate
								+ this.DAY_MAX_DATE_STR)));
			} else {
				c = new PublishDateCriteria(Range.closed(
						DateFormatUtils.getFormatDateYmdhmsByStr(publishDate
								+ this.ZERO_DATE_STR), new Date()));

			}
		} else if (iscate != null && iscate == 1) {
			// 从品类点击过来的默认不选中任何时间
			// 默认查询出所有新品
			c = new PublishDateCriteria(
					Range.closed(DateFormatUtils.getNowNotHmsBeforeByDay(
							Calendar.MONTH, -1), new Date()));

		} else {
			// publishDateType = "1";
			// if (null != newarrivalsList && newarrivalsList.size() > 0) {
			// publishDate = DateFormatUtils
			// .getDateTimeYYYYMMDD(newarrivalsList.get(0)
			// .getDateStr());
			// } else {
			// publishDate = DateFormatUtils.getDateTimeYYYYMMDD(new Date());
			// }
			//
			// c = new PublishDateCriteria(Range.closed(
			// DateFormatUtils.getFormatDateYmdhmsByStr(publishDate
			// + this.ZERO_DATE_STR),
			// DateFormatUtils.getFormatDateYmdhmsByStr(publishDate
			// + this.DAY_MAX_DATE_STR)));
			c = new PublishDateCriteria(
					Range.closed(DateFormatUtils.getNowNotHmsBeforeByDay(
							Calendar.MONTH, -1), new Date()));
		}
		ProductTagsCriteria isNew = new ProductTagsCriteria(
				ProductLabelType.NewArrial.toString());

		// 构建查询条件List
		List<Integer> categoryIdList = Lists.newArrayList();
		String[] strings = queryStrings.get("category");
		if (strings != null && strings.length == 1
				&& StringUtils.isNotEmpty(strings[0])) {
			int categoryId = Integer.parseInt(strings[0]);
			getChildCategory(categoryId, lang, site, categoryIdList);
		}

		// SearchContext context = searchFactory.fromQueryString(null,
		// queryStrings,
		// Sets.newHashSet("newarrival"));
		SearchContext context = searchFactory.fromQueryString(c, queryStrings,
				Sets.newHashSet("newarrival"));

		context.getCriteria().add(isNew);

		// Page<String> listingids = genericSearch.search(context,
		// foundation.getSiteID(), foundation.getLanguage());
		// 获取有产品的类目
		List<CategoryWebsiteWithName> catelist = categoryEnquiryService
				.getProductCatelist(Lists.newArrayList(c, isNew), null, site,
						lang);

		Page<String> listingIDs = genericSearch.search(context, site, lang);

		List<ProductBadge> badges = badgeService
				.getNewProductBadgesByListingIds(listingIDs.getList(), lang,
						site, currency, null);
		// 产品分页数据
		Map<String, ProductBadge> badgeMap = Maps.newHashMap();
		for (ProductBadge b : badges) {
			badgeMap.put(b.getListingId(), b);
		}
		Page<ProductBadge> badgePage = listingIDs.map(id -> badgeMap.get(id));

		// CategoryReverseComposite rev = categoryEnquiryService
		// .getReverseCategory(1, lang,
		// foundation.getSiteID());
		// hot列表
		List<ProductBadge> hotlist = this
				.getHotProducts(new ProductTagsCriteria(
						ProductLabelType.NewArrial.toString()));

		List<Storage> storageList = storageService.getAllStorages();

		/**
		 * get cagegoryID count
		 */
		SearchPage<String> agg = (SearchPage<String>) listingIDs;

		CategorySearchAggValue aggs = (CategorySearchAggValue) agg
				.getiSearchAggValue();

		Map<Integer, Long> curreryCatogeryCount = aggs.getCategoryCounts();
		
		return ok(views.html.product.newarrivals_new.render(badgePage,
				catelist, st, queryStrings, advertisingList, storageList,
				hotlist, context, publishDate, publishDateType,
				newarrivalsList, pciList, curreryCatogeryCount));

	}

	private List<ProductBadge> getHotProducts(ProductTagsCriteria pt) {

		// ProductTagsCriteria isNew = new ProductTagsCriteria(
		// ProductLabelType.NewArrial.toString());
		int lang = foundation.getLanguage();
		int siteid = foundation.getSiteID();
		String currency = foundation.getCurrency();
		SearchContext context = searchFactory.pureSearch(pt);
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

	private void getNewarrivalsByDate(
			List<ProductNewarrivalsCalculateItem> pciList,
			List<ProductNewarrivalsCalculateItem> newarrivalsList,
			Integer newValidDays) {
		ProductNewarrivalsCalculateItem lastWeek = productEnquiryService
				.findSumNewarrivalsByBeforeDays(DateFormatUtils
						.getNowNotHmsBeforeByDay(Calendar.WEEK_OF_MONTH, -1),
						newValidDays);

		ProductNewarrivalsCalculateItem last2Week = productEnquiryService
				.findSumNewarrivalsByBeforeDays(DateFormatUtils
						.getNowNotHmsBeforeByDay(Calendar.WEEK_OF_MONTH, -2),
						newValidDays);
		ProductNewarrivalsCalculateItem last3Week = productEnquiryService
				.findSumNewarrivalsByBeforeDays(DateFormatUtils
						.getNowNotHmsBeforeByDay(Calendar.WEEK_OF_MONTH, -3),
						newValidDays);
		ProductNewarrivalsCalculateItem lastMonth = productEnquiryService
				.findSumNewarrivalsByBeforeDays(DateFormatUtils
						.getNowNotHmsBeforeByDay(Calendar.MONTH, -1),
						newValidDays);

		// 前台scala模板中用到arraylist的先进先出的原則，添加请勿打乱順序。
		if (null != newarrivalsList && newarrivalsList.size() > 0) {

			if (null == lastWeek) {
				lastWeek = new ProductNewarrivalsCalculateItem();
				lastWeek.setNumber(0);
			}
			lastWeek.setDateStr(DateFormatUtils.getNowBeforeByDay(
					Calendar.WEEK_OF_MONTH, -1));
			pciList.add(lastWeek);

			if (null == last2Week) {
				last2Week = new ProductNewarrivalsCalculateItem();
				last2Week.setNumber(0);
			}
			last2Week.setDateStr(DateFormatUtils.getNowBeforeByDay(
					Calendar.WEEK_OF_MONTH, -2));
			pciList.add(last2Week);

			if (null == last3Week) {
				last3Week = new ProductNewarrivalsCalculateItem();
				last3Week.setNumber(0);

			}
			last3Week.setDateStr(DateFormatUtils.getNowBeforeByDay(
					Calendar.WEEK_OF_MONTH, -3));
			pciList.add(last3Week);

			if (null == lastMonth) {
				lastMonth = new ProductNewarrivalsCalculateItem();
				lastMonth.setNumber(0);

			}
			lastMonth.setDateStr(DateFormatUtils.getNowBeforeByDay(
					Calendar.MONTH, -1));
			pciList.add(lastMonth);
		}
	}

	/**
	 * @param page
	 * @return Result
	 * @Title: clearance
	 * @Description: TODO(清货首页)
	 * @author liudi
	 */
	public Result clearance() {
		MetaUtils.currentMetaBuilder().setTitle("Clearance");
		int lang = foundation.getLanguage();
		int site = foundation.getSiteID();
		String currency = foundation.getCurrency();

		ProductTagsCriteria cr = new ProductTagsCriteria(
				ProductLabelType.Clearstocks.toString());
		Map<String, String[]> req = request().queryString();
		SearchContext context = searchFactory.fromQueryCurrentHttpContext(cr,
				req);
		Page<String> listingids = genericSearch.search(context, site, lang);
		Page<ProductBadge> badgePage = listingids.batchMap(list -> badgeService
				.getNewProductBadgesByListingIds(list, lang, site, currency,
						null));

		// 获取有产品的类目
		List<CategoryWebsiteWithName> catelist = categoryEnquiryService
				.getProductCatelist(Lists.newArrayList(cr), null, site, lang);
		// hot列表
		List<ProductBadge> hotlist = this.getHotProducts(cr);
	
		
		/**
		 * get cagegoryID count
		 */
		SearchPage<String> agg = (SearchPage<String>) listingids;

		CategorySearchAggValue aggs = (CategorySearchAggValue) agg
				.getiSearchAggValue();

		Map<Integer, Long> curreryCatogeryCount = aggs.getCategoryCounts();
		
		return ok(views.html.product.clearance_new.render(badgePage, catelist,
				req, hotlist, context, curreryCatogeryCount));
	}

	/**
	 * @param page
	 * @param type
	 *            0:今天;1：明天
	 * @return Result
	 * @Title: dailyDeal
	 * @Description: TODO()
	 * @author liudi
	 */
	public Result dailyDeal(int page, int type) {
		MetaUtils.currentMetaBuilder().setTitle("Daily Deals");

		int lang = foundation.getLanguage();
		int site = foundation.getSiteID();
		String currency = foundation.getCurrency();
		final Date date = type == 1 ? DateFormatUtils.getNowBeforeByDay(
				Calendar.DAY_OF_MONTH, 1) : null;

		List<DailyDeal> dailyDeals = dailyDealEnquiryService
				.getDailyDealsByNowAfterDay(site, type);
		List<String> listingids0 = Lists.transform(dailyDeals, dailyDeal -> {
			return dailyDeal.getClistingid();
		});
		List<ProductBadge> badgeList0 = badgeService
				.getProductBadgesByListingIDs(listingids0, lang, site,
						currency, date, true, false);

		// 产品筛选逻辑:
		// a. 7天<=上架时间<=30天
		// b. 折扣>=40%
		// c. 价格<=100$
		// d. 不包含产品：特殊产品(清仓品)
		SearchContext context = searchFactory.fromQueryString(null, request()
				.queryString(), Sets.newHashSet("pager", "pricerange"));

		int endDay = -parameterService.getSystemParameterAsInt(site, lang,
				"DailyDealCreateDateLower", 7);
		int startDay = -parameterService.getSystemParameterAsInt(site, lang,
				"DailyDealCreateDateHight", 30);
		Date endDate = DateFormatUtils.getNowBeforeByDay(Calendar.DAY_OF_MONTH,
				endDay);
		Date startDate = DateFormatUtils.getNowBeforeByDay(
				Calendar.DAY_OF_MONTH, startDay);
		ISearchCriteria dc = new PublishDateCriteria(Range.closed(startDate,
				endDate));
		context.getCriteria().add(dc);

		Double discountLower = parameterService.getSystemParameterAsDouble(
				site, lang, "DailyDealDiscountRangeLower", 0.4);
		Double discountHight = parameterService.getSystemParameterAsDouble(
				site, lang, "DailyDealDiscountRangeHigher", 1.0);
		ISearchCriteria dc2 = new DisCountCriteria(Range.closed(discountLower,
				discountHight));
		context.getCriteria().add(dc2);
		Double priceLower = parameterService.getSystemParameterAsDouble(site,
				lang, "DailyDealPriceRangeLower", 0.4);
		Double priceHight = parameterService.getSystemParameterAsDouble(site,
				lang, "DailyDealPriceRangeHigher", 1.0);
		ISearchFilter dc3 = new PriceRangeFilter(Range.closed(priceLower,
				priceHight));
		context.getFilter().add(dc3);
		context.setPageSize(12);
		Page<String> listingids = genericSearch.search(context, site, lang);

		Page<ProductBadge> badgePage = listingids.batchMap(list -> badgeService
				.getProductBadgesByListingIDs(list, lang, site, currency, date,
						false, true));

		// 获取有产品的类目
		List<CategoryWebsiteWithName> catelist = categoryEnquiryService
				.getProductCatelist(Lists.newArrayList(dc, dc2),
						Lists.newArrayList(dc3), site, lang);

		List<Date> dlist = DateFormatUtils.getNowDayRange(type);
		long intdiff = type == 0 ? (dlist.get(1).getTime() - System
				.currentTimeMillis()) : (dlist.get(0).getTime() - System
				.currentTimeMillis());
		List<Html> subscribeHtmls = FluentIterable
				.from(Ordering
						.natural()
						.onResultOf(
								(ISubscribeProvider lp) -> lp.getDisplayOrder())
						.sortedCopy(subscribeProviders))
				.transform(lp -> lp.getHtml()).toList();

		return ok(views.html.product.dailydeal.render(badgeList0, badgePage,
				catelist, type, intdiff, context, request().queryString(),
				subscribeHtmls));
	}

	public Result dailyDealMore(int page, int type) {
		int lang = foundation.getLanguage();
		int site = foundation.getSiteID();
		String currency = foundation.getCurrency();
		final Date date = type == 1 ? DateFormatUtils.getNowBeforeByDay(
				Calendar.DAY_OF_MONTH, 1) : null;

		// 产品筛选逻辑:
		// a. 7天<=上架时间<=30天
		// b. 折扣>=40%
		// c. 价格<=100$
		// d. 不包含产品：特殊产品(清仓品)
		SearchContext context = searchFactory.fromQueryString(null, request()
				.queryString(), Sets.newHashSet("pager", "pricerange"));

		int endDay = -parameterService.getSystemParameterAsInt(site, lang,
				"DailyDealCreateDateLower", 7);
		int startDay = -parameterService.getSystemParameterAsInt(site, lang,
				"DailyDealCreateDateHight", 30);
		Date endDate = DateFormatUtils.getNowBeforeByDay(Calendar.DAY_OF_MONTH,
				endDay);
		Date startDate = DateFormatUtils.getNowBeforeByDay(
				Calendar.DAY_OF_MONTH, startDay);
		context.getCriteria().add(
				new PublishDateCriteria(Range.closed(startDate, endDate)));

		Double discountLower = parameterService.getSystemParameterAsDouble(
				site, lang, "DailyDealDiscountRangeLower", 0.4);
		Double discountHight = parameterService.getSystemParameterAsDouble(
				site, lang, "DailyDealDiscountRangeHigher", 1.0);
		context.getCriteria()
				.add(new DisCountCriteria(Range.closed(discountLower,
						discountHight)));
		Double priceLower = parameterService.getSystemParameterAsDouble(site,
				lang, "DailyDealPriceRangeLower", 0.4);
		Double priceHight = parameterService.getSystemParameterAsDouble(site,
				lang, "DailyDealPriceRangeHigher", 1.0);
		context.getFilter().add(
				new PriceRangeFilter(Range.closed(priceLower, priceHight)));
		context.setPageSize(12);
		Page<String> listingids = genericSearch.search(context, site, lang);

		Page<ProductBadge> badgePage = listingids.batchMap(list -> badgeService
				.getProductBadgesByListingIDs(list, lang, site, currency, date,
						false, true));
		Map<String, Object> m = new HashMap<String, Object>();
		m.put("html",
				views.html.product.dailydeal_more.render(badgePage.getList())
						.toString());
		m.put("size", badgePage.getList().size());
		m.put("page", page);
		return ok(Json.toJson(m));
	}

	public Result hot(int p, int limit) {
		MetaUtils.currentMetaBuilder().setTitle("Hot products");
		int languageid = foundation.getLanguage();
		int websiteid = foundation.getSiteID();
		ISearchCriteria pc = new ProductTagsCriteria(
				ProductLabelType.Hot.toString());
		SearchContext context = searchFactory.fromQueryCurrentHttpContext(pc,
				request().queryString());

		context.getSort().add(new DiscountSortOrder(false));
		Page<String> listingids = genericSearch.search(context, websiteid,
				languageid);
		String currency = foundation.getCurrency();
		Page<ProductBadge> badgePage = listingids.batchMap(list -> badgeService
				.getProductBadgesByListingIDs(list, languageid, websiteid,
						currency, null));
		// 获取广告
		ProductAdertisingContext pac = new ProductAdertisingContext(null, 6,
				websiteid, languageid, 3, foundation.getDevice());
		List<AdItem> advertisingList = productAdvertisingService
				.getAdvertisings(pac);

		// 获取有产品的类目
		List<CategoryWebsiteWithName> catelist = categoryEnquiryService
				.getProductCatelist(Lists.newArrayList(pc), null, websiteid,
						languageid);

		return ok(views.html.product.hot.render(advertisingList, badgePage,
				catelist, context, request().queryString()));
	}

	public Result freeShipping() {
		MetaUtils.currentMetaBuilder().setTitle("Free Shipping");
		int lang = foundation.getLanguage();
		int site = foundation.getSiteID();
		String currency = foundation.getCurrency();

		ProductTagsCriteria pt = new ProductTagsCriteria(
				ProductLabelType.FreeShipping.toString());
		SearchContext context = searchFactory.fromQueryString(pt, request()
				.queryString(), Sets.newHashSet("freeshipping"));
		// Page<String> listingids = genericSearch.search(context, site, lang);
		//
		// Page<ProductBadge> badgePage = listingids.batchMap(list ->
		// badgeService
		// .getProductBadgesByListingIDs(list, lang, site, currency, null,
		// true, true));

		Page<String> listingIDs = genericSearch.search(context, site, lang);

		List<ProductBadge> badges = badgeService
				.getNewProductBadgesByListingIds(listingIDs.getList(), lang,
						site, currency, null);
		// 产品分页数据
		Map<String, ProductBadge> badgeMap = Maps.newHashMap();
		for (ProductBadge b : badges) {
			badgeMap.put(b.getListingId(), b);
		}
		Page<ProductBadge> badgePage = listingIDs.map(id -> badgeMap.get(id));

		// 获取有产品的类目
		List<CategoryWebsiteWithName> catelist = categoryEnquiryService
				.getProductCatelist(Lists.newArrayList(pt), null, site, lang);

		// 获取广告
		ProductAdertisingContext pac = new ProductAdertisingContext(null, 8,
				foundation.getSiteID(), foundation.getLanguage(), 3,
				foundation.getDevice());
		List<AdItem> advertisingList = productAdvertisingService
				.getAdvertisings(pac);

		// hot列表
		List<ProductBadge> hotlist = this
				.getHotProducts(new ProductTagsCriteria(
						ProductLabelType.FreeShipping.toString()));

		List<Storage> storageList = storageService.getAllStorages();
		
		/**
		 * get cagegoryID count
		 */
		SearchPage<String> agg = (SearchPage<String>) listingIDs;

		CategorySearchAggValue aggs = (CategorySearchAggValue) agg
				.getiSearchAggValue();

		Map<Integer, Long> curreryCatogeryCount = aggs.getCategoryCounts();
		
		return ok(views.html.product.freeshipping_new.render(badgePage,
				catelist, context, request().queryString(), advertisingList,
				hotlist, storageList, curreryCatogeryCount));
	}

	public Result setProductStatusBySku(String sku) {
		productUpdateService.setProductStatusBySku(sku);
		return TODO;
	}

	public Result storage(Integer storageId) {
		MetaUtils.currentMetaBuilder().setTitle("Storage Shipping");
		int lang = foundation.getLanguage();
		int site = foundation.getSiteID();
		String currency = foundation.getCurrency();
		
		// 把父仓库id转换为子仓库id
		List<Storage> storages=storageService.getAllStorages();
		List<String> sonStorages=Lists.newArrayList();
		if(null!=storages&&storages.size()>0&&null!=storageId){
			storages.forEach(c->{
				if(c.getIparentstorage()==storageId){
					sonStorages.add(String.valueOf(c.getIid()));
				}
			});
		}
		StorageIdSearchCriteria searchCriteria = new StorageIdSearchCriteria(
				storageId);

		Map<String, String[]> queryStrings = request().queryString();
		SearchContext context = searchFactory.fromQueryString(searchCriteria,
				queryStrings, Sets.newHashSet("popularity"));
		if (!sonStorages.contains(null) && !sonStorages.contains("")) {
			context.getFilter().add(new StorageFilter(sonStorages));
		}

		Page<String> listingIDs = genericSearch.search(context, site, lang);

		List<ProductBadge> badges = badgeService
				.getNewProductBadgesByListingIds(listingIDs.getList(), lang,
						site, currency, null);
		// 产品分页数据
		Map<String, ProductBadge> badgeMap = Maps.newHashMap();
		for (ProductBadge b : badges) {
			badgeMap.put(b.getListingId(), b);
		}
		Page<ProductBadge> badgePage = listingIDs.map(id -> badgeMap.get(id));

		// 获取有产品的类目
		List<CategoryWebsiteWithName> catelist = categoryEnquiryService
				.getProductCatelist(Lists.newArrayList(searchCriteria), null,
						site, lang);
		// 获取广告
		ProductAdertisingContext pac = new ProductAdertisingContext(null, 8,
				foundation.getSiteID(), foundation.getLanguage(), 3,
				foundation.getDevice());
		List<AdItem> advertisingList = productAdvertisingService
				.getAdvertisings(pac);
		// hot列表
		List<ProductBadge> hotlist = this.getHotProducts(null);

		//List<Storage> storageList = storageService.getAllStorages();
		List<StorageParent> storageParents=storageParentService.getAllStorageParentList();
		Storage storage = storageService.getStorageForStorageId(storageId);
		if(null!=storageParents&&storageParents.size()>0&&null!=storageId){
			storageParents.forEach(c->{
				if(storageId==c.getIid()){
					storage.setIid(c.getIid());
					storage.setCstoragename(c.getCstoragename());
				}
			});
		}
		return ok(views.html.product.storage_new.render(badgePage, catelist,
				context, request().queryString(), advertisingList, storage,
				hotlist, storageParents));

	}

	private void getChildCategory(int categoryId, int lang, int site,
			List<Integer> categoryIdList) {
		CategoryReverseComposite rev = categoryEnquiryService
				.getReverseCategory(categoryId, lang, site);
		if (rev != null && CollectionUtils.isNotEmpty(rev.getChildren())) {
			FluentIterable.from(rev.getChildren())
					.forEach(
							item -> categoryIdList.add(item.getSelf()
									.getIcategoryid()));// 获取子品类
		}
	}
}
