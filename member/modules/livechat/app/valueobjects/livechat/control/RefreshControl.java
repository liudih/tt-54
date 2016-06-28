package valueobjects.livechat.control;


public class RefreshControl extends Control {

	String id;
	String alias;
	String ip;
	String country;

	public RefreshControl() {
		super();
		setType(ControlType.REFRESH);
	}

	public RefreshControl(String alias, String id) {
		this();
		this.alias = alias;
		this.id = id;
		setType(ControlType.REFRESH);
	}

	public RefreshControl(String id, String alias, String ip, String country) {
		this();
		this.id = id;
		this.alias = alias;
		this.ip = ip;
		this.country = country;
		setType(ControlType.REFRESH);
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

}
