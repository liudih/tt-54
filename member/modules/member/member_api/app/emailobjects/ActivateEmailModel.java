package emailobjects;

import email.model.IEmailModel;

public class ActivateEmailModel extends IEmailModel {
	private static final long serialVersionUID = 1L;

	private String toemail;
	private String url;

	public ActivateEmailModel(String emailType, Integer language,
			String toemail, String url) {
		super(emailType, language);
		this.toemail = toemail;
		this.url = url;
	}

	public String getToemail() {
		return toemail;
	}

	public void setToemail(String toemail) {
		this.toemail = toemail;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@Override
	public String toString() {
		return "ActivateEmailModel [toemail=" + toemail + ", url=" + url + "]";
	}

}
