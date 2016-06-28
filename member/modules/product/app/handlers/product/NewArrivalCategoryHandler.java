package handlers.product;

import services.product.NewArrivalCategoryService;

import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;

import events.product.NewArrivalCategoryEvent;

public class NewArrivalCategoryHandler {
	
	@Inject
	NewArrivalCategoryService newArrivalCategoryService;
	
	@Subscribe
	public void getNewArrivalCategory(NewArrivalCategoryEvent event) {
		newArrivalCategoryService.newArrivalCategory();
	}
}
