package dto;

import java.io.Serializable;
import java.util.Date;

public class StorageNameMapping implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer iid;

	private String cstoragename;

	private String cerpstoragename;

	private String ccreateuser;

	private Date dcreatedate;

	public Integer getIid() {
		return iid;
	}

	public String getCstoragename() {
		return cstoragename;
	}

	public String getCerpstoragename() {
		return cerpstoragename;
	}

	public String getCcreateuser() {
		return ccreateuser;
	}

	public Date getDcreatedate() {
		return dcreatedate;
	}

	public void setIid(Integer iid) {
		this.iid = iid;
	}

	public void setCstoragename(String cstoragename) {
		this.cstoragename = cstoragename;
	}

	public void setCerpstoragename(String cerpstoragename) {
		this.cerpstoragename = cerpstoragename;
	}

	public void setCcreateuser(String ccreateuser) {
		this.ccreateuser = ccreateuser;
	}

	public void setDcreatedate(Date dcreatedate) {
		this.dcreatedate = dcreatedate;
	}

}