package emailobjects;

import email.model.IEmailModel;

public class ReplyMemberFaqEmailModel extends IEmailModel {
	private static final long serialVersionUID = 1L;

	private String firstname;

	private String question;

	private String answer;

	public ReplyMemberFaqEmailModel(String emailType, Integer language,
			String firstname, String question, String answer) {
		super(emailType, language);
		this.firstname = firstname;
		this.question = question;
		this.answer = answer;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}
	
	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

}
