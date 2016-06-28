package dto.mobile;


public class ProductVideoInfo {
	private Integer iid;

	private String clistingid;

	private String csku;

	private String cvideourl;

	private String clabel;

	private String ccreateuser;

	private long dcreatedate;

	public Integer getIid() {
		return iid;
	}

	public void setIid(Integer iid) {
		if (iid == null) {
			iid = 0;
		}
		this.iid = iid;
	}

	public String getClistingid() {
		return clistingid;
	}

	public void setClistingid(String clistingid) {
		if (clistingid == null) {
			clistingid = "";
		}
		this.clistingid = clistingid;
	}

	public String getCsku() {
		return csku;
	}

	public void setCsku(String csku) {
		if (csku == null) {
			csku = "";
		}
		this.csku = csku;
	}

	public String getCvideourl() {
		return cvideourl;
	}

	public void setCvideourl(String cvideourl) {
		if (cvideourl == null) {
			cvideourl = "";
		}
		this.cvideourl = cvideourl;
	}

	public String getClabel() {
		return clabel;
	}

	public void setClabel(String clabel) {
		if (clabel == null) {
			clabel = "";
		}
		this.clabel = clabel;
	}

	public String getCcreateuser() {
		return ccreateuser;
	}

	public void setCcreateuser(String ccreateuser) {
		if (ccreateuser == null) {
			ccreateuser = "";
		}
		this.ccreateuser = ccreateuser;
	}

	public long getDcreatedate() {
		return dcreatedate;
	}

	public void setDcreatedate(long dcreatedate) {
		this.dcreatedate = dcreatedate;
	}
}
