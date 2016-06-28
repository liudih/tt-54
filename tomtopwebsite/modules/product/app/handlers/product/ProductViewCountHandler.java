package handlers.product;

import javax.inject.Inject;

import play.Logger;
import services.product.IProductUpdateService;

import com.google.common.eventbus.Subscribe;

import events.product.ProductViewEvent;

public class ProductViewCountHandler {

	@Inject
	IProductUpdateService service;

	@Subscribe
	public void onProductView(ProductViewEvent event) {
		try {
			String listingID = event.getProductContext().getListingID();
			int siteID = event.getProductContext().getSiteID();
			service.incrementViewCount(siteID, listingID);
		} catch (Exception e) {
			Logger.error("Increment Product View Count Error", e);
		}
	}
}
