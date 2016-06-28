package services.price;

import java.util.Date;
import java.util.List;
import java.util.Map;

import valueobjects.price.Price;
import valueobjects.price.PriceCalculationContext;
import valueobjects.product.spec.IProductSpec;

public interface IPriceService {

	Price getPrice(String listingID);

	Price getPrice(String listingID, String ccy);

	Price getPrice(IProductSpec item);

	Price getPrice(IProductSpec item, String ccy);

	List<Price> getPrice(List<IProductSpec> items);

	List<Price> getPrice(List<IProductSpec> items, Date priceAt);

	List<Price> getPrice(List<IProductSpec> items, Date priceAt, String currency);

	List<Price> getPrice(List<IProductSpec> items, PriceCalculationContext ctx);

	Map<IPriceProvider, List<Price>> getPriceByProvider(List<IProductSpec> items);

}
