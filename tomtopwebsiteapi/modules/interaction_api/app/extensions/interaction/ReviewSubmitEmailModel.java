package extensions.interaction;

import email.model.IEmailModel;

public class ReviewSubmitEmailModel extends IEmailModel{

    /**
    * @Fields serialVersionUID : TODO(用一句话描述这个变量表示什么)
    */
	private static final long serialVersionUID = 1L;
	
	private String toemail;
	
	public ReviewSubmitEmailModel(String emailType, Integer language, String toemail) {
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
