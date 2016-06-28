package forms.manager.crm;

import java.io.Serializable;

/**
 * 
 * @author lijun
 *
 */
public class MessageForm implements Serializable {
	private static final long serialVersionUID = -8179580455208841607L;

	private String subject;
	private String content;

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

}
