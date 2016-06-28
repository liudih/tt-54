package valueobjects.livechat.status;

public class ConversationStatus extends LiveChatStatus {

	public ConversationStatus() {
		super();
		this.setStatus(StatusType.CONVERSATION);
	}

}
