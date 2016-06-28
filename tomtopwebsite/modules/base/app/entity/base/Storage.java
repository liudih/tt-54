package entity.base;

public class Storage {
	Integer iid;

	Integer iparentstorage;
	

	String cstoragename;

	Integer ioverseas;

	String ccreateuser;

	public Integer getIid() {
		return iid;
	}

	public void setIid(Integer iid) {
		this.iid = iid;
	}

	public String getCstoragename() {
		return cstoragename;
	}

	public void setCstoragename(String cstoragename) {
		this.cstoragename = cstoragename;
	}

	public Integer getIoverseas() {
		return ioverseas;
	}

	public void setIoverseas(Integer ioverseas) {
		this.ioverseas = ioverseas;
	}

	public String getCcreateuser() {
		return ccreateuser;
	}

	public void setCcreateuser(String ccreateuser) {
		this.ccreateuser = ccreateuser;
	}

	public Integer getIparentstorage() {
		return iparentstorage;
	}
	
	public void setIparentstorage(Integer iparentstorage) {
		this.iparentstorage = iparentstorage;
	}
}