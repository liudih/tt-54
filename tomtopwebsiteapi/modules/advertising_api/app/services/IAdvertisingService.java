package services;

import java.util.List;

import valueobjects.product.AdItem;
import valueobjects.product.ProductAdertisingContext;
import dto.advertising.Advertising;
import dto.advertising.ProductAdertisingContextExtended;

public interface IAdvertisingService {
	List<AdItem> getAdvertisingsExtended(
			ProductAdertisingContextExtended context);
	
	List<Advertising> getProductAdvertising(ProductAdertisingContext context);
}

