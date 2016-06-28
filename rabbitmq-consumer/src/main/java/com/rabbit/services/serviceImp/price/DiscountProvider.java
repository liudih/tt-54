package com.rabbit.services.serviceImp.price;

import java.util.List;

import org.springframework.stereotype.Service;

import com.rabbit.dto.valueobjects.price.Price;
import com.rabbit.dto.valueobjects.price.PriceCalculationContext;
import com.rabbit.services.iservice.price.IDiscountProvider;
@Service
public class DiscountProvider implements IDiscountProvider {

	@Override
	public int getPriority() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<Price> decorate(List<Price> originalPrices,
			PriceCalculationContext context) {
		// TODO Auto-generated method stub
		return null;
	}

}
