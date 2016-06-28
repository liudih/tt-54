package valueobjects.livechat.status;

public class WaitStatus extends LiveChatStatus{

	private int number;
	
	public int getNumber() {
		return number;
	}

	public WaitStatus() {
		super();
		this.setStatus(StatusType.WAIT);
	}

	public WaitStatus(int number) {
		super();
		this.number = number;
		this.setStatus(StatusType.WAIT);
	}
	
}
