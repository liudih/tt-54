package valueobjects.livechat.session;

public class SimpleChatSession {

	String id;

	public SimpleChatSession(String id) {
		this.id = id;
	}

	public SimpleChatSession() {
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

}
