package emailobjects;

import email.model.IEmailModel;

public class FindPassEmailModel extends IEmailModel {
	private static final long serialVersionUID = 1L;
	
	String toemail;
	String url;
	String code;

	public FindPassEmailModel(String emailType, Integer language) {
		super(emailType, language);
	}

	public FindPassEmailModel(String emailType, Integer language,
			String toemail, String url, String code) {
		super(emailType, language);
		this.toemail = toemail;
		this.url = url;
		this.code = code;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
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
		return "FindPassEmailModel [toemail=" + toemail + ", url=" + url
				+ ", code=" + code + "]";
	}

}
