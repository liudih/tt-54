package valueobjects.manager;

import email.model.IEmailModel;

public class ReplyLeaveMessageEmailModel extends IEmailModel {

	private String content;

	private String title;

	private String question;

	private String replyUserName;

	private String questionTime;

	public ReplyLeaveMessageEmailModel(Integer language) {
		super(email.model.EmailType.LIVECHAT_LEAVEMESSAGE_REPLY.getType(),
				language);
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public String getReplyUserName() {
		return replyUserName;
	}

	public void setReplyUserName(String replyUserName) {
		this.replyUserName = replyUserName;
	}

	public String getQuestionTime() {
		return questionTime;
	}

	public void setQuestionTime(String questionTime) {
		this.questionTime = questionTime;
	}

}
