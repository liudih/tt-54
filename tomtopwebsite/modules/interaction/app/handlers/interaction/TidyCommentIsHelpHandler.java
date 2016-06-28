/**
 * 
 */
package handlers.interaction;

import play.Logger;
import services.interaction.TidyCommentIsHelpService;

import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;

import events.interaction.TidyCommentIsHelpEvent;

/**
 * @author wujirui
 *
 */
public class TidyCommentIsHelpHandler {
	@Inject
	TidyCommentIsHelpService tidyCommentIsHelpService;

	@Subscribe
	public void getTidyCommentIsHelp(TidyCommentIsHelpEvent event) {
		Logger.debug("In TidyCommentIsHelpHandler --> getTidyCommentIsHelp");

		try {
			tidyCommentIsHelpService.processCommentHelpCount();
		} catch (Exception e) {
			Logger.error("Error", e);
		}
	}

}
