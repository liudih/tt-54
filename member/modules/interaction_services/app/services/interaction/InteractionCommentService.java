/**
 * 
 */
package services.interaction;

import java.util.List;

import javax.inject.Inject;

import dto.interaction.InteractionComment;
import dto.interaction.InteractionCommentHelpEvaluate;
import dto.interaction.InteractionCommentHelpQty;
import mapper.interaction.InteractionCommentMapper;
import mapper.interaction.review.InteractionCommentHelpEvaluateMapper;
import mapper.interaction.review.InteractionCommentHelpQtyMapper;

/**
 * @author wujirui
 *
 */
public class InteractionCommentService {

	@Inject
	InteractionCommentMapper interactionCommentMapper;

	@Inject
	InteractionCommentHelpEvaluateMapper helpEvaluateMapper;

	@Inject
	InteractionCommentHelpQtyMapper helpQtyMapper;

	/**
	 * 返回查询到的评论内容字符数大于80且评论数超过5条的商品信息及评论数
	 * 
	 * @return
	 */
	public List<InteractionComment> getCommentHelpCount() {
		return interactionCommentMapper.getCommentHelpCount();
	}

	/**
	 * 根据clistingid查询
	 * 
	 * @return
	 */
	public List<InteractionComment> getCommentHelpCountByCliId(String clistingid) {
		return interactionCommentMapper.getCommentHelpCountByCliId(clistingid);
	}

	/**
	 * 插入评论有用数到表 t_interaction_comment_help_evaluate
	 * 
	 * @param list
	 * @return
	 */
	public int insertEvaluate(InteractionCommentHelpEvaluate helpEvaluate) {
		return helpEvaluateMapper.insert(helpEvaluate);
	}

	/**
	 * 根据commentId查询
	 * 
	 * @param commentId
	 * @return
	 */
	public InteractionCommentHelpQty selectByCommentId(Integer commentId) {
		return helpQtyMapper.selectByCommentId(commentId);
	}

	/**
	 * 
	 * @param helpQty
	 * @return
	 */
	public int insertHelpQty(InteractionCommentHelpQty helpQty) {
		return helpQtyMapper.insert(helpQty);
	}

	/**
	 * 
	 * @param helpQty
	 * @return
	 */
	public int updateHelpQtyByPK(InteractionCommentHelpQty helpQty) {
		return helpQtyMapper.updateByPrimaryKey(helpQty);
	}

	public List<InteractionComment> getCommentUsAudit() {
		return interactionCommentMapper.getCommentUsAudit();
	}

	public int updateStateByIds(List<Integer> list) {
		return interactionCommentMapper.updateStateByIds(list);
	}

}
