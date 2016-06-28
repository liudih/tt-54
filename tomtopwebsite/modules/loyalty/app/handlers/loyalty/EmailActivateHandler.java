package handlers.loyalty;

import com.google.common.eventbus.Subscribe;

import events.member.ActivationEvent;

/**
 * 用户邮箱激活事件监听器
 * 
 * @author lijun
 *
 */
public class EmailActivateHandler {
	@Subscribe
	public void handle(ActivationEvent event) {
		
	}
}
