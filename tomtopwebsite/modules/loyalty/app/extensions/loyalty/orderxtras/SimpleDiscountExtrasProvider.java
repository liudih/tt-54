package extensions.loyalty.orderxtras;

import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import org.elasticsearch.common.collect.FluentIterable;

import play.Logger;
import play.libs.Json;
import play.twirl.api.Html;
import services.campaign.CampaignExecutionService;
import services.campaign.IAction;
import valueobjects.order_api.ExtraLineView;
import valueobjects.order_api.ExtraSaveInfo;
import valueobjects.order_api.cart.CartItem;
import valueobjects.order_api.cart.ExtraLine;
import valueobjects.price.PriceBuilder;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.base.Optional;
import com.google.common.collect.Lists;

import dto.order.Order;
import extensions.loyalty.campaign.action.discount.SimpleDiscountAction;
import extensions.order.OrderExtrasProviderSupport;
import facades.cart.Cart;

/**
 * 具体显示和执行 SimpleDiscountAction 在购物车的折扣功能
 * 
 * @author kmtong
 * @see SimpleDiscountAction
 */
public class SimpleDiscountExtrasProvider extends OrderExtrasProviderSupport {

	public final static String ID = SimpleDiscountAction.ID;

	@Inject
	CampaignExecutionService campaignExec;

	@Override
	public int getDisplayOrder() {
		return 200;
	}

	@Override
	public String getId() {
		return ID;
	}

	@Override
	public Html renderInput(Cart cart, ExtraLine extraLine) {
		return null;
	}

	@Override
	public ExtraLineView extralineView(Cart cart, ExtraLine line) {
		if (line != null) {
			DiscountLine discount = payloadParser(line.getPayload(), cart);
			return new ExtraLineView("Discount", line.getPayload(), ID,
					-discount.getDiscountAmount());
		}
		return null;
	}

	@Override
	public ExtraSaveInfo prepareOrderInstance(Cart cart, ExtraLine line) {
		// verify discount validity
		DiscountLine discount = payloadParser(line.getPayload(), cart);

		if (discount != null) {
			return new ExtraSaveInfo(true, discount,
					-discount.getDiscountAmount(), line);
		}
		return new ExtraSaveInfo(false, line);
	}

	@Override
	public void payOperation(ExtraLine line) {
		return;
	}

	@Override
	public boolean saveOrderExtras(Order order, ExtraSaveInfo info) {
		DiscountLine line = (DiscountLine) info.getIdentifier();
		Optional<IAction> action = campaignExec.getAction(line.getCampaignId(),
				line.getCampaignInstanceId(), line.getActionId());
		Optional<Boolean> result = action.transform(a -> {
			SimpleDiscountAction da = (SimpleDiscountAction) a;
			Logger.debug("Confirming Discount: {}", info.getCartLine()
					.getPayload());
			if (da.confirm(order, info)) {
				return addBillDetail(order, "Discount", "discount",
						info.getParValue());
			}
			return false;
		});
		return result.or(false);
	}

	@Override
	public void undoSaveOrderExtras(Order order, ExtraSaveInfo info) {
		Logger.debug("DiscountExtrasProvider: Undo Nothing!");
	}

	protected DiscountLine payloadParser(String payload, Cart cart) {
		JsonNode json = Json.parse(payload);
		double amount = 0.0;
		String campaignId = json.get("campaign_id").asText();
		String instanceId = json.get("instance_id").asText();
		String actionId = json.get("action_id").asText();
		String type = null;
		Set<String> includeSku = null;
		Set<String> excludeSku = null;

		double total = cart.getTotal();

		if (json.has("include")) {
			includeSku = FluentIterable.from(json.get("include"))
					.transform(n -> n.asText()).toSet();
		}
		if (json.has("exclude")) {
			excludeSku = FluentIterable.from(json.get("exclude"))
					.transform(n -> n.asText()).toSet();
		}

		if (json.get("amount") != null) {
			amount = PriceBuilder.round_helf_up(json.get("amount").asDouble(),
					2);
			type = "amount";
		} else if (json.get("rate") != null) {
			double rate = json.get("rate").asDouble();
			if (includeSku != null || excludeSku != null) {
				total = filterTotal(cart, includeSku, excludeSku);
			}
			amount = PriceBuilder.round_helf_up(rate * total, 2);
			type = "rate";
		}
		return new DiscountLine(campaignId, instanceId, actionId, type, amount,
				json);
	}

	protected double filterTotal(Cart cart, Set<String> includeSku,
			Set<String> excludeSku) {
		List<CartItem> validItems = FluentIterable
				.from(cart.getAllItems())
				.filter(ci -> (includeSku != null) ? includeSku.contains(ci
						.getSku()) : true)
				.filter(ci -> (excludeSku != null) ? !excludeSku.contains(ci
						.getSku()) : true).toList();
		List<Double> itemPrices = Lists.transform(validItems, i -> i.getPrice()
				.getPrice());
		double total = 0.0;
		for (double d : itemPrices) {
			total += d;
		}
		return total;
	}

	public static class DiscountLine {
		final String type; // rate or amount
		final double discountAmount;
		final JsonNode json;
		final String campaignId;
		final String campaignInstanceId;
		final String actionId;

		public DiscountLine(String campaignId, String campaignInstanceId,
				String actionId, String type, double discountValue,
				JsonNode json) {
			super();
			this.campaignId = campaignId;
			this.campaignInstanceId = campaignInstanceId;
			this.actionId = actionId;
			this.type = type;
			this.discountAmount = discountValue;
			this.json = json;
		}

		public String getType() {
			return type;
		}

		public double getDiscountAmount() {
			return discountAmount;
		}

		public String getCampaignId() {
			return campaignId;
		}

		public String getCampaignInstanceId() {
			return campaignInstanceId;
		}

		public String getActionId() {
			return actionId;
		}

		public JsonNode getJson() {
			return json;
		}

	}

	@Override
	public void cancelledOperation(ExtraLine line) {
		// TODO Auto-generated method stub

	}

	@Override
	public Html renderOrderSubtotal(Cart cart, ExtraLine line) {
		// TODO Auto-generated method stub
		return null;
	}

}
