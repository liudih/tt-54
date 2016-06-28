package com.tomtop.product.price;

import java.util.Set;

import org.springframework.stereotype.Component;

import com.tomtop.product.models.dto.price.PriceCalculationContext;
import com.tomtop.product.services.price.IPriceCalculationContextProvider;

@Component
public class PriceCalculationContextFactory {

	// @Autowired
	Set<IPriceCalculationContextProvider> ctxProvider;

	public PriceCalculationContext create(String currency) {
		PriceCalculationContext ctx = new PriceCalculationContext(currency);
		for (IPriceCalculationContextProvider p : ctxProvider) {
			p.contributeTo(ctx);
		}
		return ctx;
	}
}
