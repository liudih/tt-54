package extensions.interaction.camel;

import org.apache.camel.builder.RouteBuilder;

import com.google.common.eventbus.EventBus;

import events.interaction.DailyDealsEvent;
import extensions.InjectorInstance;

public class DailyDealTimerTriggerRouteBuilder extends RouteBuilder {
	@Override
	public void configure() throws Exception {
		from("quartz2://getDailyDeals?cron=20+40+23+27+*+?").bean(this,
				"getDailyDeals");

		// 初始化数据,预计2015-5-13 20：00：00触发
		// from("quartz2://initDailyDeals?cron=20+10+20+13+5+?+2016").bean(this,
		// "initDailyDeals");

		from("quartz2://updateDailyDeals?cron=59+59+23+*+*+?").bean(this,
				"updateDailyDeals");
	}

	public void getDailyDeals() {
		InjectorInstance.getInstance(EventBus.class).post(
				new DailyDealsEvent(1, null, "USD", false));
	}

	public void initDailyDeals() {
		InjectorInstance.getInstance(EventBus.class).post(
				new DailyDealsEvent(1, null, "USD", true));
	}

	public void updateDailyDeals() {
		InjectorInstance.getInstance(EventBus.class).post(
				new DailyDealsEvent(1, "update", "USD", false));
	}
}
