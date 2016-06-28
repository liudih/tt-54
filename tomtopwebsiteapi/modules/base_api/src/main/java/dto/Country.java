package dto;

public class Country {
	Integer iid;
	String cname;
	String cshortname;
	Integer ilanguageid;
	String ccurrency;
	Integer idefaultstorage;
	Boolean bshow;

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

	public String getCshortname() {
		return cshortname;
	}

	public void setCshortname(String cshortname) {
		this.cshortname = cshortname;
	}

	public Integer getIlanguageid() {
		return ilanguageid;
	}

	public void setIlanguageid(Integer ilanguageid) {
		this.ilanguageid = ilanguageid;
	}

	public String getCcurrency() {
		return ccurrency;
	}

	public void setCcurrency(String ccurrency) {
		this.ccurrency = ccurrency;
	}

	public Integer getIdefaultstorage() {
		return idefaultstorage;
	}

	public void setIdefaultstorage(Integer idefaultstorage) {
		this.idefaultstorage = idefaultstorage;
	}

	public Boolean getBshow() {
		return bshow;
	}

	public void setBshow(Boolean bshow) {
		this.bshow = bshow;
	}

}
