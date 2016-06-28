package com.rabbit.services.serviceImp.price;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rabbit.dto.valueobjects.price.PriceCalculationContext;
import com.rabbit.services.iservice.price.IPriceCalculationContextProvider;
import com.rabbit.services.serviceImp.base.FoundationService;
@Service
public class PriceCalculationContextFactory {

	@Autowired
	Set<IPriceCalculationContextProvider> ctxProvider;

	@Autowired
	FoundationService foundation;


	public PriceCalculationContext create(String currency) {
		PriceCalculationContext ctx = new PriceCalculationContext(currency);
		for (IPriceCalculationContextProvider p : ctxProvider) {
			p.contributeTo(ctx);
		}
		return ctx;
	}
}
