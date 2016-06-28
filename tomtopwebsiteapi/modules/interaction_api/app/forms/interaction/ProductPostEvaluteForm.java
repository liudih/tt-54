package forms.interaction;

import play.data.validation.Constraints.Required;

public class ProductPostEvaluteForm {

	@Required
	private Integer ipostid;

	@Required
	private Integer ihelpfulcode;

	public Integer getIpostid() {
		return ipostid;
	}

	public void setIpostid(Integer ipostid) {
		this.ipostid = ipostid;
	}

	public Integer getIhelpfulcode() {
		return ihelpfulcode;
	}

	public void setIhelpfulcode(Integer ihelpfulcode) {
		this.ihelpfulcode = ihelpfulcode;
	}

}
