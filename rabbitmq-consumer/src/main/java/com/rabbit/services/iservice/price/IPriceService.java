package com.rabbit.services.iservice.price;

import java.util.List;

import com.rabbit.dto.valueobjects.price.Price;
import com.rabbit.dto.valueobjects.price.PriceCalculationContext;
import com.rabbit.dto.valueobjects.product.spec.IProductSpec;

public interface IPriceService {

	Price getPrice(IProductSpec item, String ccy);

	List<Price> getPrice(List<IProductSpec> items, PriceCalculationContext ctx);

}
