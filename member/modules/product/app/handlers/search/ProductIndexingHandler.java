package handlers.search;

import java.util.List;

import javax.inject.Inject;

import play.Logger;
import services.search.IndexingService;

import com.google.common.eventbus.Subscribe;

import dao.product.IProductBaseEnquiryDao;
import dto.product.ProductBase;
import events.product.ProductUpdateEvent;
import events.search.ProductIndexingRequestEvent;
import events.search.ProductSpuEvent;

public class ProductIndexingHandler {

	@Inject
	IndexingService indexing;

	@Inject
	IProductBaseEnquiryDao baseDao;

	@Subscribe
	public void onProductView(ProductUpdateEvent productUpdateEvent) {
		Logger.debug("Indexing: {} - {}",
				productUpdateEvent.getCurrentHandleType(),
				productUpdateEvent.getListingId());
		try {
			switch (productUpdateEvent.getCurrentHandleType()) {
			case insert:
			case update:
				indexing.index(productUpdateEvent.getListingId());
				break;
			case delete:
				indexing.deleteByListing(productUpdateEvent.getListingId());
				break;
			default:
				Logger.error("Unknown ProductUpdateEvent: {}",
						productUpdateEvent.getCurrentHandleType());
			}
		} catch (Exception e) {
			Logger.error("Indexing Error during processing ProductUpdateEvent",
					e);
		}
	}

	@Subscribe
	public void onIndexingRequest(ProductIndexingRequestEvent event) {
		try {
			indexing.batchIndex(event.getListingIds(), null);
		} catch (Exception e) {
			Logger.error("Indexing Request Error", e);

		}
	}

	@Subscribe
	public void onAddSpu(ProductSpuEvent event) {

		try {
			if (event != null) {
				List<ProductBase> listings = baseDao.getClistingIdBySpu(event
						.getSpu());
				for (ProductBase pBase : listings) {
					indexing.index(pBase.getClistingid());
				}
			}
		} catch (Exception e) {
			Logger.error("Indexing Spu error", e);
		}

	}

}
