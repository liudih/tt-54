package extensions.product;

import java.util.List;

import valueobjects.product.AdItem;
import valueobjects.product.ProductAdertisingContext;


public interface IProductAdvertisingProvider{
	/**
	 * 
	 * 
	 * @return
	 */
	List<AdItem> getAdvertisings(ProductAdertisingContext context);
}
