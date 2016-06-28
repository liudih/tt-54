package valueobjects.livechat.score;

import java.util.Map;

public class LiveChatSessionScore {

	private Integer id;
	private String sessionId;
	Map<Integer, Integer> questionScore;
	private String email;
	private String comment;
	private String customerLtc;
	private String customerAlias;
	private String customerServiceLtc;
	private String customerServiceAlias;
	private String topic;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public Map<Integer, Integer> getQuestionScore() {
		return questionScore;
	}

	public void setQuestionScore(Map<Integer, Integer> questionScore) {
		this.questionScore = questionScore;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getCustomerLtc() {
		return customerLtc;
	}

	public void setCustomerLtc(String customerLtc) {
		this.customerLtc = customerLtc;
	}

	public String getCustomerAlias() {
		return customerAlias;
	}

	public void setCustomerAlias(String customerAlias) {
		this.customerAlias = customerAlias;
	}

	public String getCustomerServiceLtc() {
		return customerServiceLtc;
	}

	public void setCustomerServiceLtc(String customerServiceLtc) {
		this.customerServiceLtc = customerServiceLtc;
	}

	public String getCustomerServiceAlias() {
		return customerServiceAlias;
	}

	public void setCustomerServiceAlias(String customerServiceAlias) {
		this.customerServiceAlias = customerServiceAlias;
	}

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

}
