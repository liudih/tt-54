package handlers.interaction;

import services.interaction.InteractionEnquiryService;

import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;

import events.interaction.ProductFeaturedEvent;


public class InteractionHandler {
	
	@Inject
	InteractionEnquiryService interactionEnquiryService;

	@Subscribe
	public void getFeaturedProduct(ProductFeaturedEvent event) {
		interactionEnquiryService.selectFeatured();
	}

}
