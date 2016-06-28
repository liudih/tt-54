package handlers.product;

import java.util.List;

import javax.inject.Inject;

import services.product.IProductCategoryEnquiryService;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

import events.product.CategoryRecommendEvent;
import events.search.ProductIndexingRequestEvent;
import extensions.InjectorInstance;

public class CategoryRecommendHandler {

	@Inject
	IProductCategoryEnquiryService productCategoryEnquiryService;

	@Subscribe
	public void selectCategoryRecommend(CategoryRecommendEvent event) {
		List<String> listingidlist = productCategoryEnquiryService
				.getRecommendListingid();
		InjectorInstance.getInstance(EventBus.class).post(
				new ProductIndexingRequestEvent(listingidlist));
	}
}
