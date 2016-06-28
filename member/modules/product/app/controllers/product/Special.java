package controllers.product;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.elasticsearch.common.collect.Lists;

import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import services.base.FoundationService;
import services.base.utils.MetaUtils;
import services.base.SystemParameterService;
import services.product.CategoryEnquiryService;
import services.product.IProductBadgeService;
import services.product.ProductAdvertisingCompositeEnquiry;
import services.search.ISearchService;
import services.search.SearchContextFactory;
import services.search.criteria.ProductLabelType;
import services.search.criteria.ProductTagsCriteria;
import services.search.sort.DiscountSortOrder;
import valueobjects.base.Page;
import valueobjects.product.AdItem;
import valueobjects.product.ProductAdertisingContext;
import valueobjects.product.ProductBadge;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import dto.product.CategoryWebsiteWithName;
import valueobjects.search.ISearchCriteria;
import valueobjects.search.SearchContext;
import dto.product.CategoryWebsiteWithName;

public class Special extends Controller {

	@Inject
	FoundationService foundation;

	@Inject
	ProductAdvertisingCompositeEnquiry productAdvertisingService;

	@Inject
	SearchContextFactory searchFactory;

	@Inject
	ISearchService genericSearch;

	@Inject
	IProductBadgeService badgeService;

	@Inject
	CategoryEnquiryService categoryEnquiryService;

	@Inject
	SystemParameterService parameterService;

	public Result list(String category) {

		MetaUtils.currentMetaBuilder().setTitle("Special");

		int languageid = foundation.getLanguage();
		int websiteid = foundation.getSiteID();
		String currency = foundation.getCurrency();

		ISearchCriteria dc = new ProductTagsCriteria(ProductLabelType.Special.toString());
		SearchContext sctx = searchFactory.fromQueryString(
				dc,request().queryString(), Sets.newHashSet("pager"));

		sctx.getSort().add(new DiscountSortOrder(false));
		Page<String> listingids = genericSearch.search(sctx, websiteid,
				languageid);

		List<ProductBadge> badgeList = badgeService
				.getProductBadgesByListingIDs(listingids.getList(), languageid,
						websiteid, currency, null, false, true);
		// 类目list
		List<CategoryWebsiteWithName> catelist = categoryEnquiryService.getProductCatelist(
				Lists.newArrayList(dc),null,websiteid, languageid);

		// 获取广告
		ProductAdertisingContext pac = new ProductAdertisingContext(null, 7,
				foundation.getSiteID(), foundation.getLanguage(), 3,
				foundation.getDevice());
		List<AdItem> advertisingList = productAdvertisingService
				.getAdvertisings(pac);

		int pageNo = listingids.pageNo();
		int totalPages = listingids.totalPages();

		return ok(views.html.product.special.list.render(badgeList, category,
				advertisingList, catelist, sctx, request().queryString(),
				pageNo, totalPages));
	}

	public Result asyncLoad(String categor, int page) {

		int languageid = foundation.getLanguage();
		int websiteid = foundation.getSiteID();
		String currency = foundation.getCurrency();

		SearchContext sctx = searchFactory.fromQueryString(
				new ProductTagsCriteria(ProductLabelType.Special.toString()),
				request().queryString(), Sets.newHashSet("pager"));

		Page<String> listingids = genericSearch.search(sctx, websiteid,
				languageid);
		List<ProductBadge> badgeList = badgeService
				.getProductBadgesByListingIDs(listingids.getList(), languageid,
						websiteid, currency, null, false, true);
		Map<String, Object> resultMap = Maps.newHashMap();

		List<String> productItems = Lists.newArrayList();

		for (ProductBadge p : badgeList) {
			productItems.add(views.html.product.gadget.badge_big
					.render(p, true).toString());
		}

		resultMap.put("items", productItems);
		resultMap.put("pageNo", listingids.pageNo());
		resultMap.put("pageTotail", listingids.totalPages());
		resultMap.put("size", badgeList.size());
		return ok(Json.toJson(resultMap));
	}

	public static String categoryNameToClassName(String name) {
		if (name != null && !"".equals(name)) {
			String str = name.replace(" ", "").toLowerCase();
			String[] s = str.split("&");
			if (s.length >= 2) {
				return s[0];
			}
			return str;
		}
		return "";
	}
}
