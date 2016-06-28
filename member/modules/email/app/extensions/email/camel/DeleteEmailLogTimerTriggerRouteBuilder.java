package extensions.email.camel;

import org.apache.camel.builder.RouteBuilder;

import com.google.common.eventbus.EventBus;

import events.email.DeleteEmailLogEvent;
import extensions.InjectorInstance;

public class DeleteEmailLogTimerTriggerRouteBuilder extends RouteBuilder{

	@Override
	public void configure() throws Exception {
		from("quartz2://DeleteEmailLog?cron=0+0+2+*+*+?").bean(this,
				"deleteEmailLog");
	}
	
	public void deleteEmailLog() {
		InjectorInstance.getInstance(EventBus.class).post(
				new DeleteEmailLogEvent());
	}

}
