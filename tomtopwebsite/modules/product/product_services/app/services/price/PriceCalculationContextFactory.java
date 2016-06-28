package services.price;

import java.util.Set;

import javax.inject.Inject;

import services.base.FoundationService;
import valueobjects.price.PriceCalculationContext;

public class PriceCalculationContextFactory {

	@Inject
	Set<IPriceCalculationContextProvider> ctxProvider;

	@Inject
	FoundationService foundation;

	public PriceCalculationContext create() {
		return create(foundation.getCurrency());
	}

	public PriceCalculationContext create(String currency) {
		PriceCalculationContext ctx = new PriceCalculationContext(currency);
		for (IPriceCalculationContextProvider p : ctxProvider) {
			p.contributeTo(ctx);
		}
		return ctx;
	}
}
