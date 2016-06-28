package valuesobject.mobile.member.result;

import valuesobject.mobile.BaseJson;

public class LoginJson extends BaseJson {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private MobileMember info;

	public MobileMember getInfo() {
		return info;
	}

	public void setInfo(MobileMember info) {
		this.info = info;
	}

}

