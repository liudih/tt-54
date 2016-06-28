package services.home.fragment;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import play.Logger;
import play.mvc.Http.Context;
import play.twirl.api.Html;
import services.base.FoundationService;
import services.base.template.ITemplateFragmentProvider;
import services.base.utils.DateFormatUtils;
import services.product.IProductBadgeService;
import services.search.IDailyDealEnquiryService;
import services.search.ISearchContextFactory;
import services.search.ISearchService;
import utils.HttpSendRequest;
import valueobjects.product.ProductBadge;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;

import dto.product.DailyDeal;

public class DailyDealsFragmentProvider implements ITemplateFragmentProvider {
	@Inject
	ISearchService genericSearch;
	@Inject
	ISearchContextFactory searchFactory;
	@Inject
	FoundationService foundation;

	@Inject
	IDailyDealEnquiryService dailyDealEnquiryService;

	@Inject
	IProductBadgeService badgeService;

	@Override
	public String getName() {
		return "daily-deals";
	}

	@Override
	public Html getFragment(Context context) {
		int type = 0;
		int lang = foundation.getLanguage();
		int site = foundation.getSiteID();
		String currency = foundation.getCurrency();
		final Date date = type == 1 ? DateFormatUtils.getNowBeforeByDay(
				Calendar.DAY_OF_MONTH, 1) : null;

//		List<DailyDeal> dailyDeals = dailyDealEnquiryService
//				.getDailyDealsByNowAfterDay(site, type);
//		List<String> listingids0 = Lists.transform(dailyDeals, dailyDeal -> {
//			return dailyDeal.getClistingid();
//		});
		List<String> listingids0 = Lists.newArrayList();
		Logger.debug(date+"=====================空指针===");
		String dateStr = org.apache.commons.lang3.time.DateFormatUtils.formatUTC(new Date(), "yyyy/MM/dd");
		String getDailyDealUrl = "http://product.api.tomtop.com/ic/v1/home/dailyDeal?"
				+ "currency=" + currency + "&client=1&lang=1&date=" + dateStr;
		try {
			String dailyDealJson = HttpSendRequest.sendGet(getDailyDealUrl);

			ObjectMapper om = new ObjectMapper();
			JsonNode jsonNode = om.readTree(dailyDealJson);

			JsonNode node = jsonNode.get("data");
			if (node != null) {
				if (node.isArray()) {
					java.util.Iterator<JsonNode> list = node.iterator();
					while (list.hasNext()) {
						JsonNode pi = list.next();
						String listingId = pi.get("listingId") == null ? ""
								: pi.get("listingId").asText();

						Logger.debug(listingId+"=========123================================");
						listingids0.add(listingId);
					}
				}

			}
		} catch (Exception e) {
			Logger.error("get dailyDealJson form error", e);
		}
		List<ProductBadge> list = badgeService.getProductBadgesByListingIDs(
				listingids0, lang, site, currency, date, false, true);
		return views.html.home.daily_deals.render(list);
	}

}
