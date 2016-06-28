package valueobjects.livechat.control;

public class PingControl extends Control {

	String alias;

	boolean biswait;

	public PingControl(String alias, boolean biswait) {
		super();
		this.alias = alias;
		this.biswait = biswait;
		setType(ControlType.PING);
	}

	public PingControl() {
		super();
		setType(ControlType.PING);
	}

	public boolean isBiswait() {
		return biswait;
	}

	public void setBiswait(boolean biswait) {
		this.biswait = biswait;
	}

	public PingControl(String alias) {
		this();
		this.alias = alias;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

}
