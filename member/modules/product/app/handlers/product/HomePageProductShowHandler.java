package handlers.product;

import services.product.IHomePageProductShowHistoryService;

import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;

import events.product.HomePageProductShowEvent;

public class HomePageProductShowHandler {

	@Inject
	IHomePageProductShowHistoryService homePageProductShowHistoryService;

	@Subscribe
	public void insertHomePageProductShowData(HomePageProductShowEvent event) {
		homePageProductShowHistoryService.insertOrUpdateHomePageProductShowData();
	}

}
