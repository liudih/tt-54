package valueobjects.livechat.control;

public class AcceptControl extends Control {

	String alias;

	/**
	 * 主题
	 */
	String topic;

	public AcceptControl() {
		super();
		setType(ControlType.ACCEPT);
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

}
