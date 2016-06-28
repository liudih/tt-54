package dto.member.login;

import java.io.Serializable;

public class OtherLoginParameter implements IThirdLoginParameter, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	String code;
	String state;
	String reredirectUri;

	public OtherLoginParameter(String code, String state, String reredirectUri) {
		super();
		this.code = code;
		this.state = state;
		this.reredirectUri = reredirectUri;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getReredirectUri() {
		return reredirectUri;
	}

	/**
	 * 
	 * @param reredirectUri
	 */
	public void setReredirectUri(String reredirectUri) {
		this.reredirectUri = reredirectUri;
	}

}
