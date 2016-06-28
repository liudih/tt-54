package valueobjects.livechat.data;


/**
 * Used for HTTP communications by stripping sensitive data.
 * 
 * @author kmtong
 *
 */
public class SimpleMessage {

	String text;
	private String senderAlias;
	private String sendDate;

	public SimpleMessage() {
	}

	public SimpleMessage(String text) {
		this.text = text;
	}
	
	public SimpleMessage(String text, String senderAlias, String sendDate) {
		this.text = text;
		this.senderAlias = senderAlias;
		this.sendDate = sendDate;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getSenderAlias() {
		return senderAlias;
	}

	public void setSenderAlias(String senderAlias) {
		this.senderAlias = senderAlias;
	}

	public String getSendDate() {
		return sendDate;
	}

	public void setSendDate(String sendDate) {
		this.sendDate = sendDate;
	}

}
