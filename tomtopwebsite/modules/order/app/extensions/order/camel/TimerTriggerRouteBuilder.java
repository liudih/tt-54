package extensions.order.camel;

import org.apache.camel.builder.RouteBuilder;

import com.google.common.eventbus.EventBus;

import events.order.AutoBundlingEvent;
import events.order.ProductsaleEvent;
import events.order.RemindPaymentEvent;
import extensions.InjectorInstance;

public class TimerTriggerRouteBuilder extends RouteBuilder {

	@Override
	public void configure() throws Exception {
		// TOO SLOW
		//from("quartz2://autobundlisting?cron=0+0+*/20+*+*+?").bean(this,
		//		"autoBundling");
//		from("quartz2://hotproduct?cron=0+0+*/20+*+*+?").bean(this,
//				"selectHotProduct");
//		from("quartz2://remindpayment?cron=0+0+*/12+*+*+?").bean(this,
//				"remindUserToPayment");
	}

	public void autoBundling() {
		InjectorInstance.getInstance(EventBus.class).post(
		// XXX hard code site ID = 1
				new AutoBundlingEvent(1));
	}

	public void selectHotProduct() {
		InjectorInstance.getInstance(EventBus.class).post(
		// XXX hard code site ID = 1
				new ProductsaleEvent(1));
	}

	public void remindUserToPayment() {
		InjectorInstance.getInstance(EventBus.class).post(
				new RemindPaymentEvent());
	}
}
