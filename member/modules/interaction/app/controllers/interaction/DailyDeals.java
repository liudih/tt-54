package controllers.interaction;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import services.base.FoundationService;
import services.interaction.dailydeal.DailyDealsService;

import com.google.inject.Inject;

import dao.product.IDailyDealEnquiryDao;
import dto.product.DailyDeal;

public class DailyDeals extends Controller {
	@Inject
	DailyDealsService dealsService;
	@Inject
	FoundationService foundation;
	@Inject
	IDailyDealEnquiryDao dailyDealEnquiryService;

	public Result dailyDeal() {
		Integer websiteId = foundation.getSiteID();
		List<DailyDeal> dailyDeals = dailyDealEnquiryService
				.getListingIdByWebsiteId(websiteId, 3, 0);
		Map<String, String> resultMap = new HashMap<String, String>();
		if (dailyDeals.size() > 0) {
			resultMap.put("error", "Has been performed");
			return ok(Json.toJson(resultMap));
		}
		resultMap = dealsService.getDailyDeals(websiteId, "USD", true);

		return ok(Json.toJson(resultMap));
	}

	public Result updateDailyDeals() {
		Integer websiteId = foundation.getSiteID();
		boolean updateDailyDeals = dealsService.updateDailyDeals(websiteId);
		HashMap<String, Boolean> resultMap = new HashMap<String, Boolean>();

		resultMap.put("result", updateDailyDeals);

		return ok(Json.toJson(updateDailyDeals));
	}
}
