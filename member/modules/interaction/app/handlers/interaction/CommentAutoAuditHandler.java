/**
 * 
 */
package handlers.interaction;

import play.Logger;
import services.interaction.CommentAutoAuditService;

import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;

import events.interaction.CommentAutoAuditEvent;

/**
 * @author wujirui
 *
 */
public class CommentAutoAuditHandler {
	@Inject
	CommentAutoAuditService commentAutoAuditService;

	@Subscribe
	public void getCommentAutoAudit(CommentAutoAuditEvent event) {
		Logger.info("In class CommentAutoAuditHandler --> method:getCommentAutoAudit----------");
		try {
			commentAutoAuditService.commentAutoAudit();
		} catch (Exception e) {
			Logger.error("Error", e);
		}
	}

}
