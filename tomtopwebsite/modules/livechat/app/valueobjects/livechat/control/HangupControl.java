package valueobjects.livechat.control;

public class HangupControl extends Control {

	String id;

	public HangupControl() {
		super();
		setType(ControlType.HANGUP);
	}

	public HangupControl(String id) {
		this();
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

}
