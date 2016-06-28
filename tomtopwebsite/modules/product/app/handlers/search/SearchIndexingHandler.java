package handlers.search;

import javax.inject.Inject;

import play.Logger;
import services.search.IndexingService;

import com.google.common.eventbus.Subscribe;

import events.product.ProductViewEvent;

public class SearchIndexingHandler {

	@Inject
	IndexingService indexing;

	@Subscribe
	public void onProductView(ProductViewEvent event) {
		try {
			Logger.debug("Reindexing product {}", event.getProductContext()
					.getListingID());
			indexing.update(event.getProductContext().getListingID(),
					"ctx._source.viewCount+=1");
		} catch (Exception e) {
			Logger.error("ProductView Reindexing Error", e);
		}
	}
}
