package forms.mobile;

import play.data.validation.Constraints.Required;

public class ClientErrorLogForm {

	@Required
	private String error;

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

}
