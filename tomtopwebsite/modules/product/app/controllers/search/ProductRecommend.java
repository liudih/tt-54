package controllers.search;

import java.util.List;

import javax.inject.Inject;

import play.libs.F.Promise;
import play.mvc.Controller;
import play.mvc.Result;
import services.product.IProductCategoryEnquiryService;

import com.google.common.eventbus.EventBus;

import events.search.ProductIndexingRequestEvent;

public class ProductRecommend extends Controller {

	@Inject
	IProductCategoryEnquiryService productCategoryEnquiryService;
	@Inject
	EventBus eventBus;

	public Promise<Result> resetindexing() {
		List<String> listingidlist = productCategoryEnquiryService
				.getRecommendListingidAll();
		eventBus.post(new ProductIndexingRequestEvent(listingidlist));
		return Promise.promise(() -> {
			return ok("Reset Done");
		});
	}
}
