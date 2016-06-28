package valueobjects.livechat.control;

public class WaitControl extends Control {

	int number;

	public WaitControl() {
		super();
		setType(ControlType.WAIT);
	}

	public WaitControl(int number) {
		super();
		this.number = number;
		setType(ControlType.WAIT);
	}

	public int getNumber() {
		return number;
	}

}
