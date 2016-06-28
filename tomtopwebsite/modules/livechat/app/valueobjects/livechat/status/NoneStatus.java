package valueobjects.livechat.status;

public class NoneStatus extends LiveChatStatus {

	public NoneStatus() {
		super();
		this.setStatus(StatusType.NONE);
	}
}
