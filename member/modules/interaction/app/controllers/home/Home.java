package controllers.home;

import java.util.List;

import org.elasticsearch.common.collect.FluentIterable;

import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Http.Context;
import play.mvc.Result;
import services.base.FoundationService;
import services.home.SuperDealsEnquiry;
import services.interaction.MemberBrowseHistoryService;
import services.interaction.superdeal.SuperDealService;
import services.product.CategoryEnquiryService;
import services.product.IProductBadgeService;
import valueobjects.base.Page;
import valueobjects.product.ProductBadge;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.Lists;
import com.google.inject.Inject;

import filters.common.CookieTrackingFilter;

public class Home extends Controller {
	@Inject
	SuperDealsEnquiry superDealsQuery;

	@Inject
	IProductBadgeService productBadgeService;

	@Inject
	FoundationService foundationService;

	@Inject
	CategoryEnquiryService categoryService;

	@Inject
	MemberBrowseHistoryService memberBrowseHistoryService;

	@Inject
	SuperDealsEnquiry superDealsEnquiry;

	@Inject
	SuperDealService superdealservice;

	@BodyParser.Of(BodyParser.Json.class)
	public Result getNextSuperDeals() {
		JsonNode json = request().body().asJson();
		Integer currentPage = json.get("currentPage").asInt();
		Integer perPage = json.get("perPage").asInt();
		String liClass = json.get("liClass").asText();
		int lang = foundationService.getLanguage();
		int siteId = foundationService.getSiteID();
		String ccy = foundationService.getCurrency();

		List<String> listingIdList = Lists.newArrayList();
		Context ctx = Context.current();
		String cookieID = CookieTrackingFilter.getLongTermCookie(ctx);

		List<String> list = superdealservice.getSuperDealListingIds(24, siteId);

		if (cookieID != null) {
			String lastViewListingId = memberBrowseHistoryService
					.getLastViewListingIdBySiteIdAndLtc(siteId, cookieID);
			if (null != lastViewListingId) {
				Integer rootCategoryId = categoryService
						.getRootCategoryIdBySiteIdAndListingId(siteId,
								lastViewListingId);
				if (null != rootCategoryId) {
					Page<String> pageListing = superdealservice.getSDPageBySiteId(siteId,rootCategoryId,currentPage + 1,perPage);
					listingIdList = pageListing.getList();
				}
			}
		}
		if (list != null && listingIdList != null) {
			listingIdList.removeIf(p -> p == null || list.contains(p));
		}
		List<ProductBadge> productBadges = productBadgeService
				.getProductBadgesByListingIDs(listingIdList, lang, siteId, ccy,
						null);

		return ok(views.html.home.super_deals_box
				.render(liClass, productBadges));
	}
}
