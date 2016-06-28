
package extensions.interaction.camel;

import org.apache.camel.builder.RouteBuilder;

import com.google.common.eventbus.EventBus;

import events.interaction.SuperDealEvent;
import extensions.InjectorInstance;

public class SuperDealTimerTriggerRouteBuilder extends RouteBuilder {

	@Override
	public void configure() throws Exception {
	/*	from("quartz2://selectSuperDealProducts?cron=10+30+01+*+*+?").bean(this,
				"selectSuperDealProducts");*/
	}

	public void selectSuperDealProducts() {
		Integer siteId = 1;
		Integer languageId = 1;
		InjectorInstance.getInstance(EventBus.class).post(
				new SuperDealEvent(siteId, languageId));
	}
}
