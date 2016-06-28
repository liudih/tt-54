package handlers.interaction;

import javax.inject.Inject;

import play.Logger;
import services.search.IndexingService;

import com.google.common.eventbus.Subscribe;

import events.interaction.ProductReviewEvent;
import extensions.interaction.search.InteractionIndexProvider;

/**
 * 
 * @author kmtong
 * @see InteractionIndexProvider
 */
public class ReviewCountIndexHandler {

	@Inject
	IndexingService indexing;

	@Subscribe
	public void onReview(ProductReviewEvent event) {
		Logger.debug("Review Count +1 on Product {}", event.getListingID());
		indexing.update(event.getListingID(),
				"ctx._source.extras.interactions.reviewCount+=1");
	}
}
