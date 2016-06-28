package handlers.product;

import services.product.IMerchantsService;

import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;

import events.product.MerchantsProductEvent;

public class MerchantsProductEventHandler {
	
	@Inject
	IMerchantsService merchantsService;
	/**
	 * 自动更新
	 * @param event
	 */
	@Subscribe
	public void merchantsProductService(MerchantsProductEvent event) {
		merchantsService.pushMerchantsProductList();
	}
}
