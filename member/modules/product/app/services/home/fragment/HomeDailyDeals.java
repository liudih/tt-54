package services.home.fragment;

import java.util.List;

import javax.inject.Inject;

import dto.product.DailyDeal;
import play.mvc.Http.Context;
import play.twirl.api.Html;
import services.base.utils.DateFormatUtils;
import services.base.FoundationService;
import services.base.template.ITemplateFragmentProvider;
import services.product.IProductBadgeService;
import services.product.ProductDailySaleBannerService;
import services.search.IDailyDealEnquiryService;
import valueobjects.product.ProductBadge;

public class HomeDailyDeals implements ITemplateFragmentProvider {

	@Inject
	FoundationService foundation;

	@Inject
	IDailyDealEnquiryService dailyDealEnquiryService;

	@Inject
	IProductBadgeService productBadgeService;

	@Override
	public String getName() {
		return "daily-deals";
	}

	@Inject
	ProductDailySaleBannerService productDailySaleBannerService;

	@Override
	public Html getFragment(Context context) {
		int site = 1;
		int lang = 1;
		if (context != null) {
			site = foundation.getSiteID(context);
			lang = foundation.getLanguage(context);
		}
		String currency = foundation.getCurrency(context);
		List<DailyDeal> dailyDeals = dailyDealEnquiryService
				.getDailyDealsByNowAfterDay(site, 0);
		ProductBadge product = new ProductBadge();
		if (null != dailyDeals && dailyDeals.size() > 0) {
			product = productBadgeService.getByListing(dailyDeals.get(0)
					.getClistingid(), lang,currency);
		} else {
			product = null;
		}

		long intdiff = DateFormatUtils.getNowDayRange(0).get(1).getTime()
				- System.currentTimeMillis();
		return views.html.home.daily_deals.render(context, product, intdiff);
	}

}
