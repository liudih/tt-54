package events.email;

import java.io.Serializable;

public class EmailResendEvent implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String from;
	private String toEmail;
	private String campaignName;
	private String title;
	private String content;

	public EmailResendEvent(String from, String toEmail, String title,
			String content) {
		this.from = from;
		this.toEmail = toEmail;
		this.title = title;
		this.content = content;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getToEmail() {
		return toEmail;
	}

	public void setToEmail(String toEmail) {
		this.toEmail = toEmail;
	}

	public String getCampaignName() {
		return campaignName;
	}

	public void setCampaignName(String campaignName) {
		this.campaignName = campaignName;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

}
