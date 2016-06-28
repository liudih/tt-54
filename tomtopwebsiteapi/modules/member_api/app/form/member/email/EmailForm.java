package form.member.email;

import java.io.Serializable;

import play.data.validation.Constraints.Required;

public class EmailForm implements Serializable {
	private static final long serialVersionUID = 1L;

	@Required
	private String cemail;

	public String getCemail() {
		return cemail;
	}

	public void setCemail(String cemail) {
		this.cemail = cemail;
	}
}
