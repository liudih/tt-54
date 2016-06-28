package handlers.product;

import javax.inject.Inject;

import services.product.inventory.InventoryUpdateService;

import com.google.common.eventbus.Subscribe;

import dto.product.InventoryHistory;
import events.product.InventoryChangeEvent;

public class InventoryChangeHandler {

	@Inject
	InventoryUpdateService service;

	@Subscribe
	public void onInventoryChange(InventoryChangeEvent event) {
		if (event.getList() != null) {
			for (InventoryHistory ih : event.getList()) {
				service.insert(ih);
			}
		} else {
			service.insert(event.getListingID(), event.getRemark(),
					event.getQty(), event.getWebsiteID(), event.getType());
		}
	}
}
