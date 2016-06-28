package valueobjects.livechat.session;

/**
 * Internal ChatSession Representation for persistence to Redis.
 * 
 * @author kmtong
 *
 */
public class ChatSession {

	String id;

	/**
	 * LTC that originates this chat session
	 */
	String origin;

	/**
	 * LTC that accepts this chat session
	 */
	String destination;

	/**
	 * 会话主题 
	 */
	String topic;

	public ChatSession(String id) {
		this.id = id;
	}

	public ChatSession() {
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getOrigin() {
		return origin;
	}

	public void setOrigin(String origin) {
		this.origin = origin;
	}

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	@Override
	public String toString() {
		return "ChatSession [id=" + id + ", origin=" + origin
				+ ", destination=" + destination + ", topic=" + topic + "]";
	}

}
