package com.rabbit.services.serviceImp.price;

import java.util.List;

import org.springframework.stereotype.Service;

import com.rabbit.dto.valueobjects.price.Price;
import com.rabbit.dto.valueobjects.price.PriceCalculationContext;
import com.rabbit.dto.valueobjects.product.spec.IProductSpec;
import com.rabbit.services.iservice.price.IPriceProvider;
@Service
public class PriceProvider implements IPriceProvider {

	@Override
	public List<Price> getPrice(List<IProductSpec> items,
			PriceCalculationContext ctx) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean match(IProductSpec item) {
		// TODO Auto-generated method stub
		return false;
	}

}
