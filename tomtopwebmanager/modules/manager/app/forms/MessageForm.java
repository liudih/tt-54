package forms;

import play.data.validation.Constraints.Required;

/**
 * 
 * @author lijun
 *
 */
public class MessageForm {
	private String id;
	@Required
	private String subject;
	@Required
	private String content;
	private String type;
	
	private String cemail;
	
	
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
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
	public String getCemail() {
		return cemail;
	}
	public void setCemail(String cemail) {
		this.cemail = cemail;
	}
	
	
}
