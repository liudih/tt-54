package valueobjects.livechat.control;

public class TransferControl extends Control {

	String id;
	String to;
	Integer number;
	String topic;

	public Integer getNumber() {
		return number;
	}

	public void setNumber(Integer number) {
		this.number = number;
	}

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	public TransferControl() {
		super();
		setType(ControlType.TRANSFER);
	}

	public TransferControl(String id, String to, int number, String topic) {
		this();
		this.id = id;
		this.to = to;
		this.number = number;
		this.topic = topic;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

}
