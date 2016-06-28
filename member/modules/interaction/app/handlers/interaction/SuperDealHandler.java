package handlers.interaction;

import services.interaction.superdeal.SuperDealService;

import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;

import events.interaction.SuperDealEvent;

public class SuperDealHandler {

	@Inject
	SuperDealService superDealService;

	/**
	 * @author jiang 自动更新super deal数据，如果super_deal表中的数据是手工添加，则不更新
	 */
	@Subscribe
	public void addNewSuperDealProduct(SuperDealEvent event) {
		superDealService.handleSuperDeal(event.getSiteId(),
				event.getLangaugeId());
	}
}
