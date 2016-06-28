package dto.mobile;

public class VersionInfo {

	private String vs;

	private String descr;

	private String durl;

	public String getVs() {
		return vs == null ? "" : vs;
	}

	public void setVs(String vs) {
		this.vs = vs;
	}

	public String getDescr() {
		return descr == null ? "" : descr;
	}

	public void setDescr(String descr) {
		this.descr = descr;
	}

	public String getDurl() {
		return durl == null ? "" : durl;
	}

	public void setDurl(String durl) {
		this.durl = durl;
	}

}
