package extensions.livechat.topic;

public class ChatTopic {

	final String destinationAlias;

	final String displayKey;

	public ChatTopic(String destinationAlias, String displayKey) {
		super();
		this.destinationAlias = destinationAlias;
		this.displayKey = displayKey;
	}

	public String getDestinationAlias() {
		return destinationAlias;
	}

	public String getDisplayKey() {
		return displayKey;
	}

}
