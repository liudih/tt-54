package extensions.loyalty.campaign.rule.totalexceed;

import javax.inject.Inject;

import play.Logger;
import services.base.CurrencyService;
import services.campaign.CampaignContext;
import services.campaign.IActionRule;
import services.campaign.IActionRuleParameter;
import valueobjects.price.PriceOnly;
import codec.ICodec;

import com.fasterxml.jackson.databind.JsonNode;

import facades.cart.Cart;

public class TotalExceedActionRule implements IActionRule {

	public static final String ID = "total-exceed";

	@Inject
	CurrencyService service;

	@Inject
	TotalExceedActionRuleParameterCodec codec;

	@Override
	public String getId() {
		return ID;
	}

	@Override
	public boolean match(CampaignContext context, IActionRuleParameter param) {
		Cart cart = (Cart) context.getActionOn();
		TotalExceedActionRuleParameter p = (TotalExceedActionRuleParameter) param;
		Logger.debug("Total Exceed Parameter: {}", p);
		PriceOnly price = cart.getTotalPrice();
		double amount = service.exchange(price.getPrice(), price.getCurrency(),
				p.getCurrency());
		Logger.debug("Total Exceed: {} (check against: {})", amount,
				p.getAmount());
		return (amount >= p.getAmount());
	}

	@Override
	public ICodec<IActionRuleParameter, JsonNode> getParameterCodec() {
		return codec;
	}

}
