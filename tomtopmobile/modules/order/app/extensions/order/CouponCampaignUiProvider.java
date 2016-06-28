package extensions.order;

import play.twirl.api.Html;
import services.base.FoundationService;
import services.loyalty.IPointsService;

import com.google.inject.Inject;

/**
 * 优惠券使用UI
 * 
 * @author lijun
 *
 */
public class CouponCampaignUiProvider implements CampaignUiProvider {
	private final static String name = "coupon";

	@Inject
	IPointsService pointsService;

	@Inject
	FoundationService foundationService;

	@Override
	public Html render(CampaignUiContext ctx) {
		return Html.apply("");
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getExtraLineId() {
		return "coupon";
	}

	@Override
	public Html renderExtraLine(CampaignUiContext ctx) {
		return null;
	}
}
