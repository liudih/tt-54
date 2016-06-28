package events.member;

import java.io.Serializable;

public class ReplyMemberFaqEvent implements Serializable {

	private static final long serialVersionUID = 1L;
	final String toemail;
	final String question;
	final String answer;
	final Integer iwebsiteid;
	final Integer ilanguageid;

	public ReplyMemberFaqEvent(String toemail, String question, String answer,
			Integer iwebsiteid, Integer ilanguageid) {
		super();
		this.toemail = toemail;
		this.question = question;
		this.answer = answer;
		this.iwebsiteid = iwebsiteid;
		this.ilanguageid = ilanguageid;
	}

	public String getToemail() {
		return toemail;
	}

	public Integer getIwebsiteid() {
		return iwebsiteid;
	}

	public Integer getIlanguageid() {
		return ilanguageid;
	}

	public String getQuestion() {
		return question;
	}

	public String getAnswer() {
		return answer;
	}
	
}
