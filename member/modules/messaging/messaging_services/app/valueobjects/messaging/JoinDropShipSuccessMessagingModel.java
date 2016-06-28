package valueobjects.messaging;

import email.model.IEmailModel;

public class JoinDropShipSuccessMessagingModel extends IEmailModel {

	private String firstname;
	
	public JoinDropShipSuccessMessagingModel(String emailType, Integer language, String firstname) {
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
