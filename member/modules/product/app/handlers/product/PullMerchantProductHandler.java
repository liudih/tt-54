package handlers.product;

import services.product.IMerchantsService;

import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;

import events.product.PullMerchantProductEvent;

public class PullMerchantProductHandler {
	
	@Inject
	IMerchantsService merchantsService;
	/**
	 * 通过谷歌品类，自动刊登新品
	 * @param event
	 */
	@Subscribe
	public void pullProductEventService(PullMerchantProductEvent event) {
		merchantsService.pullMerchantsProductList();
	}
}
