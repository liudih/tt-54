package emailobjects;

import email.model.IEmailModel;

public class JoinWholeSaleFailureEmailModel extends IEmailModel {
	private static final long serialVersionUID = 1L;

	private String firstname;

	public JoinWholeSaleFailureEmailModel(String emailType, Integer language,
			String firstname) {
		super(emailType, language);
		this.firstname = firstname;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

}
