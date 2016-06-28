package extensions.loyalty.campaign.action.point;

import javax.inject.Inject;

import play.Logger;
import services.base.CurrencyService;
import services.base.utils.StringUtils;
import services.campaign.CampaignContext;
import services.campaign.IAction;
import services.campaign.IActionParameter;
import services.loyalty.IPointsService;
import valueobjects.member.MemberIdentification;
import valueobjects.order_api.OrderValue;
import codec.ICodec;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * Can be applied to an email or an order.
 * 
 * @author kmtong
 *
 */
public class GrantPointAction implements IAction {

	public final static String ID = "grant-point";

	@Inject
	GrantPointActionParameterCodec codec;

	@Inject
	IPointsService pointService;

	@Inject
	CurrencyService currencyService;

	@Override
	public String getId() {
		return ID;
	}

	@Override
	public void execute(CampaignContext context, IActionParameter param) {
		GrantPointActionParameter gp = (GrantPointActionParameter) param;
		String source = gp.getSource();
		Object o = context.getActionOn();
		if (o instanceof MemberIdentification) {
			int point = gp.getPoints();
			int status = gp.getStatus();
			String type = context.getInstance().getCampaign().getId();
			String remark = context.getInstance().getInstanceId();
			MemberIdentification mid = (MemberIdentification) o;
			Logger.debug("Grant Point Action: {}", point);
			pointService.grantPoints(mid.getEmail(), mid.getSiteID(), point,
					type, remark, status, source);
		} else if (o instanceof OrderValue) {
			OrderValue order = (OrderValue) o;
			double usd = currencyService.exchange(order.getOrder()
					.getFgrandtotal(), order.getOrder().getCcurrency(), "USD");
			double amount = Math.floor(usd);
			double points = gp.getRate() * amount;
			if (points <= 0) {
				return;
			}
			Logger.debug("Grant Point Action (Order Amount: {}): {}", amount,
					points);
			String type = context.getInstance().getInstanceId() + ".No."
					+ order.getOrder().getCordernumber();
			String email = order.getOrder().getCmemberemail();
			if (StringUtils.isEmpty(email)) {
				email = order.getOrder().getCemail();
			}
			pointService.grantPoints(email, order.getOrder().getIwebsiteid(),
					points, context.getInstance().getCampaign().getId(), type,
					1, source);
		}
	}

	@Override
	public ICodec<IActionParameter, JsonNode> getParameterCodec() {
		return codec;
	}

}
