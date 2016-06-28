package dto;

import java.io.Serializable;
import java.util.Date;

public class Platform implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Integer iid;
	String ccode;
	String cremarks;
	Integer iparentid;
	Integer ilevel;
	Integer ichildrencount;
	String ccreateuser;
	Date dcreatedate;
	
	public Platform(Integer iid,String ccode) {
		this.iid=iid;
		this.ccode = ccode;
	}

	public Integer getIid() {
		return iid;
	}

	public void setIid(Integer iid) {
		this.iid = iid;
	}

	public String getCcode() {
		return ccode;
	}

	public void setCcode(String ccode) {
		this.ccode = ccode;
	}

	public String getCremarks() {
		return cremarks;
	}

	public void setCremarks(String cremarks) {
		this.cremarks = cremarks;
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

	public Integer getIchildrencount() {
		return ichildrencount;
	}

	public void setIchildrencount(Integer ichildrencount) {
		this.ichildrencount = ichildrencount;
	}

	public String getCcreateuser() {
		return ccreateuser;
	}

	public void setCcreateuser(String ccreateuser) {
		this.ccreateuser = ccreateuser;
	}

	public Date getDcreatedate() {
		return dcreatedate;
	}

	public void setDcreatedate(Date dcreatedate) {
		this.dcreatedate = dcreatedate;
	}

}
