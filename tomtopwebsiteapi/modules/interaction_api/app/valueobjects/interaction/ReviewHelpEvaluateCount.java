package valueobjects.interaction;

import java.io.Serializable;

public class ReviewHelpEvaluateCount implements Serializable  {
	private static final long serialVersionUID = 1L;
	private Integer commentId;
	private Integer helpfulCount;
	private Integer notHelpfulCount;
	public Integer getCommentId() {
		return commentId;
	}
	public void setCommentId(Integer commentId) {
		this.commentId = commentId;
	}
	public Integer getHelpfulCount() {
		return helpfulCount;
	}
	public void setHelpfulCount(Integer helpfulCount) {
		this.helpfulCount = helpfulCount;
	}
	public Integer getNotHelpfulCount() {
		return notHelpfulCount;
	}
	public void setNotHelpfulCount(Integer notHelpfulCount) {
		this.notHelpfulCount = notHelpfulCount;
	}
	
	
}
