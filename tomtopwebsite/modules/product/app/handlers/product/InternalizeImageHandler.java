package handlers.product;

import javax.inject.Inject;

import play.Logger;
import services.product.ProductImageService;

import com.google.common.collect.Lists;
import com.google.common.eventbus.Subscribe;

import events.product.ProductUpdateEvent;
import events.product.ProductUpdateEvent.ProductHandleType;

public class InternalizeImageHandler {

	@Inject
	ProductImageService service;

	@Subscribe
	public void onProductUpdate(ProductUpdateEvent event) {
		try {
			if (event.getCurrentHandleType() == ProductHandleType.insert) {
				service.internalizeProductImages(Lists.newArrayList(event
						.getListingId()));
			}
		} catch (Exception e) {
			Logger.error("Internalize Image Error", e);
		}
	}
}
