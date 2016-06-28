package services.member.fragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import play.mvc.Http.Context;
import play.twirl.api.Html;
import services.base.FoundationService;
import services.base.template.ITemplateFragmentProvider;
import services.home.SuperDealsEnquiry;
import services.interaction.IMemberBrowseHistoryService;
import services.interaction.superdeal.SuperDealService;
import services.product.CategoryEnquiryService;
import services.product.IHomePageDataEnquiry;
import services.product.IProductBadgeService;
import valueobjects.product.ProductBadge;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.inject.Inject;

import filters.common.CookieTrackingFilter;

public class SuperDealsProvider implements ITemplateFragmentProvider {

	@Inject
	IHomePageDataEnquiry homePageDataEnquiry;

	@Inject
	SuperDealsEnquiry superDealsEnquiry;

	@Inject
	IProductBadgeService badge;

	@Inject
	FoundationService foundation;

	@Inject
	IMemberBrowseHistoryService memberBrowseHistoryService;

	@Inject
	CategoryEnquiryService categoryService;

	@Inject
	SuperDealService superdealservice;

	@Override
	public String getName() {
		return "super-deals-in-member";
	}

	@Override
	public Html getFragment(Context context) {
		String cookieID = CookieTrackingFilter.getLongTermCookie(context);
		Integer perPage = 5;
		int siteId = 1;
		int lang = 1;
		String ccy = "USD";
		if (context != null) {
			siteId = foundation.getSiteID(context);
			lang = foundation.getLanguage(context);
			ccy = foundation.getCurrency(context);
		}
		Integer maxPage = 4;
		String divClass = "xxkBOX accH_box block";
		String divId = "accH_box";
		String ulClass = "accMovebox";
		String liClass = "accMovePic";

		String nextPageAjaxUrl = controllers.home.routes.Home
				.getNextSuperDeals().url();
		List<ProductBadge> badges = new ArrayList<ProductBadge>();
		Set<String> allListingIds = Sets.newHashSet();

		// 从后台取出来的数据
		List<String> list = superdealservice.getSuperDealListingIds(perPage
				* maxPage, siteId);
		if (null != list && list.size() > 0) {
			allListingIds.addAll(list);
		}

		if (null != allListingIds && allListingIds.size() >= perPage) {
			badges = badge.getProductBadgesByListingIDs(
					Lists.newArrayList(allListingIds), lang, siteId, ccy, null);
			return views.html.home.turn_next_page_model.render(context, badges,
					maxPage, perPage, nextPageAjaxUrl, null, divClass, divId,
					ulClass, liClass, true);
		}

		// 判断cookie中是否有值，存在则推荐该用户浏览历史最后一个产品所在品类的产品
		List<String> listingIds = superdealservice
				.getListingsByLastViewRootCateogryId(cookieID, perPage, siteId,
						lang);
		allListingIds.addAll(listingIds);
		if (null != allListingIds && allListingIds.size() >= perPage) {
			badges = badge.getProductBadgesByListingIDs(
					Lists.newArrayList(allListingIds), lang, siteId, ccy, null);
			return views.html.home.turn_next_page_model.render(context, badges,
					maxPage, perPage, nextPageAjaxUrl, null, divClass, divId,
					ulClass, liClass, true);
		}

		// 每类显示一个产品（前提保证搜索引擎中每个类别都有数据）
		allListingIds = superdealservice.getOneListingIdEveryRootCategory(
				siteId, lang, allListingIds);

		if (allListingIds.size() > 0) {
			badges = badge.getProductBadgesByListingIDs(
					Lists.newArrayList(allListingIds), lang, siteId, ccy, null);
		}

		return views.html.home.turn_next_page_model.render(context, badges,
				maxPage, perPage, nextPageAjaxUrl, null, divClass, divId,
				ulClass, liClass, true);
	}
}
