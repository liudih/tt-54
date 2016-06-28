package extensions.interaction.camel;

import org.apache.camel.builder.RouteBuilder;

import com.google.common.eventbus.EventBus;

import events.interaction.ProductFeaturedEvent;
import extensions.InjectorInstance;

public class InteractionTimerTrigger extends RouteBuilder {
	@Override
	public void configure() throws Exception {

		/*from("quartz2://selectFeaturedProduct?cron=10+30+03+*+*+?").bean(this,
				"selectFeaturedProduct");*/
	}

	public void selectFeaturedProduct() {
		InjectorInstance.getInstance(EventBus.class).post(
				new ProductFeaturedEvent());
	}

}
