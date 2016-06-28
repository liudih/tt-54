package com.rabbit.services.serviceImp.price;

import org.springframework.stereotype.Service;

import com.rabbit.dto.valueobjects.price.PriceCalculationContext;
import com.rabbit.services.iservice.price.IPriceCalculationContextProvider;
@Service
public class PriceCalculationContextProvider implements
		IPriceCalculationContextProvider {

	@Override
	public void contributeTo(PriceCalculationContext context) {
		// TODO Auto-generated method stub

	}

}
