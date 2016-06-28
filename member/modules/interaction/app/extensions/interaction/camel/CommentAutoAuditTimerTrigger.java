/**
 * 
 */
package extensions.interaction.camel;

import org.apache.camel.builder.RouteBuilder;

import play.Logger;
import services.interaction.InteractionCommentService;

import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;

import events.interaction.CommentAutoAuditEvent;
import extensions.InjectorInstance;

/**
 * @author wujirui 用于自动审核评论信息
 */
public class CommentAutoAuditTimerTrigger extends RouteBuilder {

	@Inject
	InteractionCommentService interactionCommentService;

	@Override
	public void configure() throws Exception {
		/*
		 * from("quartz2://commentAutoAudit?cron=0+0+23+*+*+?").bean(this,
		 * "processAutoAudit");
		 */
	}

	public void processAutoAudit() {
		Logger.debug("---------In class CommentAutoAuditTimerTrigger-----processAutoAudit-----start");
		InjectorInstance.getInstance(EventBus.class).post(
				new CommentAutoAuditEvent());
	}

}
