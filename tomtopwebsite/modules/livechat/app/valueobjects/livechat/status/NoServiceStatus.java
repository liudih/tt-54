package valueobjects.livechat.status;

public class NoServiceStatus extends LiveChatStatus {

	public NoServiceStatus() {
		super();
		this.setStatus(status.NOSERVICE);
	}
}
