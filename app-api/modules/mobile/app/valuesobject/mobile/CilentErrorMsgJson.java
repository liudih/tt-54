package valuesobject.mobile;

import java.util.List;

public class CilentErrorMsgJson {

	private List<CilentErrorMsgDetailJson> list;

	private String sysvs;

	private String phonename;

	private int appid;

	private int curvs;

	public List<CilentErrorMsgDetailJson> getList() {
		return list;
	}

	public void setList(List<CilentErrorMsgDetailJson> list) {
		this.list = list;
	}

	public String getSysvs() {
		return sysvs;
	}

	public void setSysvs(String sysvs) {
		this.sysvs = sysvs;
	}

	public String getPhonename() {
		return phonename;
	}

	public void setPhonename(String phonename) {
		this.phonename = phonename;
	}

	public int getAppid() {
		return appid;
	}

	public void setAppid(int appid) {
		this.appid = appid;
	}

	public int getCurvs() {
		return curvs;
	}

	public void setCurvs(int curvs) {
		this.curvs = curvs;
	}

}
