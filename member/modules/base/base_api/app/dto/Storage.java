package dto;

import java.io.Serializable;

public class Storage implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	Integer iid;

	String cstoragename;

	Integer ioverseas;

	String ccreateuser;
	
	Integer iparentstorage;

	public Integer getIid() {
		return iid;
	}

	public Integer getIparentstorage() {
		return iparentstorage;
	}

	public void setIparentstorage(Integer iparentstorage) {
		this.iparentstorage = iparentstorage;
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

}