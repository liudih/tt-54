package extensions.product.camel;

import org.apache.camel.builder.RouteBuilder;

import com.google.common.eventbus.EventBus;

import events.product.NewArrivalCategoryEvent;
import extensions.InjectorInstance;

public class NewArrivalTimerTriggerRouteBuilder extends RouteBuilder {

	@Override
	public void configure() throws Exception {
		from("quartz2://newArrivalCategory?cron=0+0+15+*+*+?").bean(this,
				"selectNewArrivalCategory");
	}

	public void selectNewArrivalCategory() {
		InjectorInstance.getInstance(EventBus.class).post(
				new NewArrivalCategoryEvent());
	}

}
