package entity.manager;

public class AdminMenu {
	private Integer iid;
	private String cname;
	private Integer iparentid;
	private Integer ilevel;
	private String cclass;
	private String curl;
	private String ccreateuser;

	public Integer getIid() {
		return iid;
	}

	public void setIid(Integer iid) {
		this.iid = iid;
	}

	public String getCname() {
		return cname;
	}

	public void setCname(String cname) {
		this.cname = cname;
	}

	public Integer getIparentid() {
		return iparentid;
	}

	public void setIparentid(Integer iparentid) {
		this.iparentid = iparentid;
	}

	public Integer getIlevel() {
		return ilevel;
	}

	public void setIlevel(Integer ilevel) {
		this.ilevel = ilevel;
	}

	public String getCclass() {
		return cclass;
	}

	public void setCclass(String cclass) {
		this.cclass = cclass;
	}

	public String getCcreateuser() {
		return ccreateuser;
	}

	public void setCcreateuser(String ccreateuser) {
		this.ccreateuser = ccreateuser;
	}

	public String getCurl() {
		return curl;
	}

	public void setCurl(String curl) {
		this.curl = curl;
	}

}
