/**
 * 
 */
package services.interaction;

import java.util.ArrayList;
import java.util.List;

import play.Logger;

import com.google.inject.Inject;

import dto.interaction.InteractionComment;

/**
 * @author Administrator
 *
 */
public class CommentAutoAuditService {

	@Inject
	InteractionCommentService interactionCommentService;

	/**
	 * 自动审批
	 */
	public void commentAutoAudit() {
		Logger.info("---------In class CommentAutoAuditService --> method:commentAutoAudit---------");
		List<InteractionComment> commentList = interactionCommentService
				.getCommentUsAudit();
		if (null == commentList || commentList.size() <= 0) {
			return;
		}

		Logger.info("commentAutoAudit-----------commentList size = "
				+ commentList.size());
		List<Integer> idList = null;
		int count = getAuditCount(commentList.size());
		Logger.info("commentAutoAudit-----------commentCount= " + count);
		for (int i = 0; i < count; i++) {
			idList = new ArrayList<Integer>();
			idList.add(commentList.get(i).getIid());
			interactionCommentService.updateStateByIds(idList);
		}
	}

	/**
	 * 计算每次自动审批的评论数
	 * 
	 * @param listCount
	 * @return
	 */
	private int getAuditCount(int listCount) {
		int count = listCount;
		if (listCount <= 10) {
			return count;
		} else {
			int tempCount = (int) Math.floor(listCount * 0.2);
			count = count + tempCount;
		}
		return count;
	}

}
