package extensions.product.camel;

import org.apache.camel.builder.RouteBuilder;

import com.google.common.eventbus.EventBus;

import events.product.HomePageProductShowEvent;
import extensions.InjectorInstance;

public class HomePageShowTimerTriggerRouteBuilder extends RouteBuilder {

	@Override
	public void configure() throws Exception {
//		from("quartz2://homePageProductShowData?cron=0+0+2+*+*+?").bean(this,
//				"insertHomePageProductShowData");
	}

	public void insertHomePageProductShowData() {
		InjectorInstance.getInstance(EventBus.class).post(
				new HomePageProductShowEvent());
	}
}
