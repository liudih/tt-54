package emailobjects;

import email.model.IEmailModel;

public class RegEmailModel extends IEmailModel{
	private static final long serialVersionUID = 1L;

	private String toemail;
	
	public RegEmailModel(String emailType, Integer language, String toemail) {
		super(emailType, language);
		this.toemail = toemail;
	}

	public String getToemail() {
		return toemail;
	}

	public void setToemail(String toemail) {
		this.toemail = toemail;
	}
	
	
}
