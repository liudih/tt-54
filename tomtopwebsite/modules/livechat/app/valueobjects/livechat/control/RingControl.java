package valueobjects.livechat.control;

public class RingControl extends Control {

	String originAlias;

	/**
	 * 主题
	 */
	String topic;

	public RingControl() {
		super();
		setType(ControlType.RING);
	}

	public RingControl(String originAlias, String topic) {
		this();
		this.originAlias = originAlias;
		this.topic = topic;
		setType(ControlType.RING);
	}

	public String getOriginAlias() {
		return originAlias;
	}

	public void setOriginAlias(String originAlias) {
		this.originAlias = originAlias;
	}

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

}
