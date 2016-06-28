package com.tomtop.product.services.price;

import java.util.Date;
import java.util.List;

import com.tomtop.product.models.dto.price.Price;
import com.tomtop.product.models.dto.price.PriceCalculationContext;

public interface IPriceService {

	Price getPrice(String listingID, String ccy);

	Price getPrice(IProductSpec item, String ccy);

	Price getPrice(IProductSpec item, PriceCalculationContext ccy);

	List<Price> getPrice(List<IProductSpec> items, Date priceAt, String currency);

	List<Price> getPrice(List<IProductSpec> items, PriceCalculationContext ctx);
}
