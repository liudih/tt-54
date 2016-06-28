package valueobjects.livechat.control;

public class ClosedControl extends Control {

	String id;

	public ClosedControl() {
		super();
		setType(ControlType.CLOSED);
	}

	public ClosedControl(String id) {
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
