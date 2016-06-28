package valueobjects.livechat.control;

public class EstablishedControl extends Control {

	String id;
	String otherAlias;

	public EstablishedControl() {
		super();
		setType(ControlType.ESTABLISHED);
	}

	public EstablishedControl(String id, String otherAlias) {
		this();
		this.id = id;
		this.otherAlias = otherAlias;
		setType(ControlType.ESTABLISHED);
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getOtherAlias() {
		return otherAlias;
	}

	public void setOtherAlias(String otherAlias) {
		this.otherAlias = otherAlias;
	}

}
