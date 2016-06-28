package forms.interaction.review;

import java.io.Serializable;

public class CommentsEvaluteForm implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Integer icommentid;

	private Integer helpfulcode;

	public Integer getIcommentid() {
		return icommentid;
	}

	public void setIcommentid(Integer icommentid) {
		this.icommentid = icommentid;
	}

	public Integer getHelpfulcode() {
		return helpfulcode;
	}

	public void setHelpfulcode(Integer helpfulcode) {
		this.helpfulcode = helpfulcode;
	}

}
