package handlers.search;

import javax.inject.Inject;

import play.Logger;
import services.search.IndexingService;

import com.google.common.eventbus.Subscribe;

import events.product.SaleEvent;

public class SaleIndexHandler {

	@Inject
	IndexingService indexing;

	@Subscribe
	public void onSaleReview(SaleEvent event) {
		try {
			indexing.update(event.getListingId(),
					"ctx._source.extras.sales.sale+=1");
		} catch (Exception e) {
			Logger.error("Sale Item Increment Error: " + event.getListingId(),
					e);
		}
	}
}
