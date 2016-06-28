package controllers.interaction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import play.Logger;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http.Context;
import play.mvc.Result;
import play.twirl.api.Html;
import services.base.FoundationService;
import services.base.utils.MetaUtils;
import services.interaction.superdeal.SuperDealService;
import services.product.CategoryEnquiryService;
import services.product.IProductBadgeService;
import services.product.ProductEnquiryService;
import services.search.ISearchService;
import services.search.SearchContextFactory;
import services.search.criteria.DiscountOnlySearchCriteria;
import valueobjects.base.Page;
import valueobjects.product.ProductBadge;
import valueobjects.search.ISearchCriteria;
import valueobjects.search.SearchContext;

import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;
import com.google.common.collect.Sets;

import dto.product.CategoryWebsiteWithName;
import extensions.product.subscribe.ISubscribeProvider;
import filters.common.CookieTrackingFilter;

public class SuperDeals extends Controller {
	@Inject
	SearchContextFactory contextFactory;

	@Inject
	ISearchService searchService;

	@Inject
	FoundationService foundation;
	@Inject
	IProductBadgeService badgeService;
	@Inject
	ProductEnquiryService productEnquiryService;
	@Inject
	CategoryEnquiryService categoryEnquiryService;
	@Inject
	Set<ISubscribeProvider> subscribeProviders;

	@Inject
	SuperDealService superDealService;

	public Result superDeal(int page, int limit) {
		Logger.info("superDeal-----start-----");
		MetaUtils.currentMetaBuilder().setTitle("Super Deals");
		int lang = foundation.getLanguage();
		int site = foundation.getSiteID();
		String currency = foundation.getCurrency();

		// ===new methods test begin==========
		Context context0 = Context.current();
		String cookieID = CookieTrackingFilter.getLongTermCookie(context0);
		Integer perPage = 10;
		List<ProductBadge> badgePageList = new ArrayList<ProductBadge>();
		List<String> listingIds1 = Lists.newArrayList();
		List<String> listingId2 = superDealService.getSuperDealListingIds(perPage,
				site);
		if (null != listingId2 && listingId2.size() > 0) {
			listingIds1.addAll(listingId2);
		}
		List<String> listingByView = superDealService
				.getListingsByLastViewRootCateogryId(cookieID, perPage, site,
						lang);
		listingIds1.addAll(listingByView);
		if (null != listingIds1 && listingIds1.size() > 0) {
			Set<String> listingSet = ImmutableSet.copyOf(listingIds1);
			listingIds1 = ImmutableSet.copyOf(listingSet).asList();
			badgePageList = badgeService.getProductBadgesByListingIDs(
					listingIds1, lang, site, currency, null);
		}

		Page<ProductBadge> badgePage = new Page<ProductBadge>(badgePageList,
				perPage, 0, 2);
		// ====new methods test over=================

		// 滑动效果上的产品
		// SearchContext context = contextFactory.fromQueryCurrentHttpContext(
		// new DiscountOnlySearchCriteria(),
		// new HashMap<String, String[]>());
		// context.setPage(0);
		// context.setPageSize(10);
		// context.getSort().add(new DiscountSortOrder(false)); //
		// 根据折扣数最大降序排列，也就是对客户最实惠
		// context.getSort().add(new ReviewCountSortOrder(false)); // 根据评论数降序排
		// Page<String> listingids = searchService.search(context,
		// foundation.getSiteID(), foundation.getLanguage());
		// Page<ProductBadge> badgePage = listingids.batchMap(list ->
		// badgeService
		// .getProductBadgesByListingIDs(list, lang, site, currency, null,
		// true, true));

		// 筛选的产品
		//ISearchCriteria dc = new DiscountOnlySearchCriteria();
		SearchContext context2 = contextFactory.fromQueryString(null, request()
				.queryString(), Sets.newHashSet("pager"));
		context2.setPageSize(limit);
		Logger.info("new-DiscountOnlySearchCriteria--4--");
		Page<String> listingids2 = searchService.search(context2,
				foundation.getSiteID(), foundation.getLanguage());
		Logger.info("new-DiscountOnlySearchCriteria--5.4--{}",listingids2.getList().size());
		Page<ProductBadge> badgePage2 = listingids2
				.batchMap(list -> badgeService.getProductBadgesByListingIDs(
						list, lang, site, currency, null, false, true));
		Logger.info("new-DiscountOnlySearchCriteria--6--");
		// 类目list
		List<CategoryWebsiteWithName> catelist = categoryEnquiryService
				.getProductCatelist(null, null, site, lang);
		List<Html> subscribeHtmls = FluentIterable
				.from(Ordering
						.natural()
						.onResultOf(
								(ISubscribeProvider lp) -> lp.getDisplayOrder())
						.sortedCopy(subscribeProviders))
				.transform(lp -> lp.getHtml()).toList();
		return ok(views.html.home.super_deals.render(badgePage, badgePage2,
				context2, catelist, request().queryString(), subscribeHtmls));
	}

	public Result superDealMore(int page) {
		int lang = foundation.getLanguage();
		int site = foundation.getSiteID();
		String currency = foundation.getCurrency();

		SearchContext context2 = contextFactory.fromQueryCurrentHttpContext(
				new DiscountOnlySearchCriteria(), request().queryString());

		Page<String> listingids2 = searchService.search(context2,
				foundation.getSiteID(), foundation.getLanguage());
		Page<ProductBadge> badgePage2 = listingids2
				.batchMap(list -> badgeService.getProductBadgesByListingIDs(
						list, lang, site, currency, null, false, true));
		Map<String, Object> m = new HashMap<String, Object>();
		StringBuffer sb = new StringBuffer();
		for (ProductBadge p : badgePage2.getList()) {
			sb.append(views.html.product.gadget.badge_big.render(p, true)
					.toString());
		}
		m.put("html", sb.toString());
		m.put("size", badgePage2.getList().size());
		m.put("page", page);
		m.put("listings", String.join(",", listingids2.getList()));
		return ok(Json.toJson(m));
	}
}
