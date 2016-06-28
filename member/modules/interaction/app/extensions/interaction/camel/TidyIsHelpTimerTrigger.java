/**
 * 
 */
package extensions.interaction.camel;

import org.apache.camel.builder.RouteBuilder;

import play.Logger;
import services.interaction.InteractionCommentService;

import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;

import events.interaction.TidyCommentIsHelpEvent;
import extensions.InjectorInstance;

/**
 * @author wujirui desc: 定时任务，根据评论生成有用数和无用数 规则如下： 按评论数级别设置有用，无用数 5--20, 随机1个有用;
 *         21--50, 随机3个有用 51--100, 随机5个有用，一个无用 101以上，随机5个有用，两个无用 date:2015-06-12
 */
public class TidyIsHelpTimerTrigger extends RouteBuilder {

	@Inject
	InteractionCommentService interactionCommentService;

	@Override
	public void configure() throws Exception {
		from("quartz2://processCommentHelpCount?cron=0+0+01+*+*+?").bean(this,
				"processComment");
	}

	public void processComment() {
		Logger.debug("In class TidyIsHelpTimerTrigger --> processComment-----start");
		InjectorInstance.getInstance(EventBus.class).post(
				new TidyCommentIsHelpEvent());
	}

}
