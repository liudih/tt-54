package extensions.loyalty.campaign.action.discount;

import java.util.Map;

import javax.inject.Inject;

import play.Logger;
import play.libs.Json;
import services.campaign.CampaignContext;
import services.campaign.IAction;
import services.campaign.IActionParameter;
import valueobjects.order_api.ExtraSaveInfo;
import valueobjects.order_api.cart.ExtraLine;
import codec.ICodec;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.Maps;

import dto.order.Order;
import facades.cart.Cart;

/**
 * 简单打折，不用扣除券号和变更任何状态，没有数量限制
 * 
 * @author kmtong
 * @see extensions.loyalty.orderxtras.SimpleDiscountExtrasProvider
 */
public class SimpleDiscountAction implements IAction {

	public static final String ID = "simple-discount";

	@Inject
	SimpleDiscountActionParameterCodec codec;

	@Override
	public String getId() {
		return ID;
	}

	@Override
	public void execute(CampaignContext context, IActionParameter param) {
		SimpleDiscountActionParameter p = (SimpleDiscountActionParameter) param;
		Logger.debug("Order Discount for Cart: {}, type: {}, discount: {}",
				context.getActionOn(), p.getType(), p.getValue());
		Cart c = (Cart) context.getActionOn();
		if (c.getExtraLines().containsKey(ID)) {
			Logger.debug("Already Discounted, ignoring");
			return;
		}
		Map<String, Object> payload = Maps.newHashMap();
		payload.put("campaign_id", context.getInstance().getCampaign().getId());
		payload.put("instance_id", context.getInstance().getInstanceId());
		payload.put("action_id", getId());
		switch (p.getType()) {
		case WHOLE:
			switch (p.getUnit()) {
			case AMOUNT:
				ExtraLine amount = new ExtraLine();
				amount.setPluginId(ID);

				payload.put("amount", p.getValue());
				amount.setPayload(Json.toJson(payload).toString());

				c.addExtraLine(amount);
				break;
			case PERCENT:
				ExtraLine percent = new ExtraLine();
				percent.setPluginId(ID);

				double rate = p.getValue();
				// prevent overshoot
				if (rate >= 0.0 && rate <= 1.0) {
					payload.put("rate", rate);
					percent.setPayload(Json.toJson(payload).toString());

					c.addExtraLine(percent);
				}
				break;
			}
			break;
		case SKU_INCLUSIVE:
			switch (p.getUnit()) {
			case PERCENT:
				ExtraLine percent = new ExtraLine();
				percent.setPluginId(ID);

				double rate = p.getValue();
				// prevent overshoot
				if (rate >= 0.0 && rate <= 1.0) {
					payload.put("rate", rate);
					payload.put("include", p.getIncludeSku());
					percent.setPayload(Json.toJson(payload).toString());

					c.addExtraLine(percent);
				}
				break;
			case AMOUNT:
				Logger.warn("SKU discount cannot be AMOUNT");
				break;
			}
			break;
		case SKU_EXCLUSIVE:
			switch (p.getUnit()) {
			case PERCENT:
				ExtraLine percent = new ExtraLine();
				percent.setPluginId(ID);

				double rate = p.getValue();
				// prevent overshoot
				if (rate >= 0.0 && rate <= 1.0) {
					payload.put("rate", rate);
					payload.put("exclude", p.getExcludeSku());
					percent.setPayload(Json.toJson(payload).toString());

					c.addExtraLine(percent);
				}
				break;
			case AMOUNT:
				Logger.warn("SKU discount cannot be AMOUNT");
				break;
			}
			break;
		default:
			break;
		}
	}

	public boolean confirm(Order order, ExtraSaveInfo info) {
		Logger.debug("Confirm discount!");
		return true;
	}

	@Override
	public ICodec<IActionParameter, JsonNode> getParameterCodec() {
		return codec;
	}

}
