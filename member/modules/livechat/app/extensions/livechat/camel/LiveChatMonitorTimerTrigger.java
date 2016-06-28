package extensions.livechat.camel;

import org.apache.camel.builder.RouteBuilder;

import com.google.common.eventbus.EventBus;

import events.livechat.LiveChatUnactiveSessionEvent;
import extensions.InjectorInstance;

public class LiveChatMonitorTimerTrigger extends RouteBuilder {

	@Override
	public void configure() throws Exception {
		// 30 second
		from("quartz2://monitorLiveChatUnactiveUser?cron=*/30+*+*+*+*+?").bean(
				this, "monitorLiveChatUnactiveUser");
	}

	public void monitorLiveChatUnactiveUser() {
		InjectorInstance.getInstance(EventBus.class).post(
				new LiveChatUnactiveSessionEvent());

	}

}
