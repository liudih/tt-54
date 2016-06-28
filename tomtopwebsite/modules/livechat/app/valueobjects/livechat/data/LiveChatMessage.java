package valueobjects.livechat.data;

import java.util.Date;

/**
 * Used for internal object passing.
 * 
 * @author kmtong
 * @see SimpleMessage
 */
public class LiveChatMessage {

	String chatSessionID;
	String from;
	String to;
	String text;
	Date timestamp;
	String fromAlias;
	String toAlias;
	String topic;

	public String getChatSessionID() {
		return chatSessionID;
	}

	public void setChatSessionID(String chatSessionID) {
		this.chatSessionID = chatSessionID;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public String getFromAlias() {
		return fromAlias;
	}

	public void setFromAlias(String fromAlias) {
		this.fromAlias = fromAlias;
	}

	public String getToAlias() {
		return toAlias;
	}

	public void setToAlias(String toAlias) {
		this.toAlias = toAlias;
	}

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

}
