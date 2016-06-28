package extensions.base.camel;

import org.apache.camel.builder.RouteBuilder;

import com.google.common.eventbus.EventBus;

import event.base.CurrencyRateEvent;
import extensions.InjectorInstance;

public class TimerTriggerRouteBuilder extends RouteBuilder {

	@Override
	public void configure() throws Exception {
		from("quartz2://getCurrencyRate?cron=30+5+*+*+*+?").bean(this,
				"getCurrencyRateTrigger");
	}

	public void getCurrencyRateTrigger() {
		InjectorInstance.getInstance(EventBus.class).post(
				new CurrencyRateEvent());
	}
}
