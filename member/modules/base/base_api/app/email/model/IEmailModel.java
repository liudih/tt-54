package email.model;

import java.io.Serializable;

public abstract class IEmailModel implements Serializable {
	private static final long serialVersionUID = 1L;
	String emailType;
	Integer language;
	

	public IEmailModel(String emailType, Integer language) {
		super();
		this.emailType = emailType;
		this.language = language;
	}

	public String getEmailType() {
		return emailType;
	}

	public Integer getLanguage() {
		return language;
	}

	public void setEmailType(String emailType) {
		this.emailType = emailType;
	}

	public void setLanguage(Integer language) {
		this.language = language;
	}

}
