package valuesobject.mobile;

public class HandshakeJson extends BaseJson {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String token;

	private String currency;

	private String timezone;

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getTimezone() {
		return timezone;
	}

	public void setTimezone(String timezone) {
		this.timezone = timezone;
	}

}