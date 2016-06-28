package extensions.order;

import play.mvc.Http.Context;
import play.twirl.api.Html;
import services.base.FoundationService;
import services.loyalty.IPointsService;
import valueobjects.base.LoginContext;
import valueobjects.order_api.OrderConstants;
import valueobjects.order_api.cart.ExtraLine;

import com.google.inject.Inject;

import context.ContextUtils;
import context.WebContext;
import dto.Currency;

/**
 * 积分使用UI
 * 
 * @author lijun
 *
 */
public class PointCampaignUiProvider implements CampaignUiProvider {
	private final static String name = "point";

	@Inject
	IPointsService pointsService;

	@Inject
	FoundationService foundationService;

	@Override
	public Html render(CampaignUiContext ctx) {
		return views.html.mobile.cart.point_campaign.render(ctx);
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getExtraLineId() {
		return OrderConstants.POINTS;
	}

	@Override
	public Html renderExtraLine(CampaignUiContext ctx) {
		LoginContext loginCtx = foundationService.getLoginContext();
		if (loginCtx == null || !loginCtx.isLogin()) {
			return Html.apply("");
		}
		String email = loginCtx.getMemberID();
		ExtraLine el = ctx.getExtraLine();
		WebContext webCtx = ContextUtils.getWebContext(Context.current());
		if (ctx.getExtraLine() != null) {
			Currency cc = foundationService.getCurrencyObj();
			Integer points = Integer.valueOf(el.getPayload());
			Double money = pointsService.getMoney(points, email, webCtx);
			return views.html.mobile.cart.point_subtotal.render(money, cc,
					points);
		}
		return Html.apply("");
	}
}
