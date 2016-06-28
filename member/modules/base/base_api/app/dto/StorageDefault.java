package dto;

import java.io.Serializable;
import java.util.Date;

/**
 * 地区默认仓库
 * 
 * @author Administrator
 *
 */
public class StorageDefault implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Integer iid;
	// 仓库ID
	Integer idefaultstorage;
	// 地区
	String ccountryname;

	String ccreateuser;

	Date dcreatedate;

	public Integer getIdefaultstorage() {
		return idefaultstorage;
	}

	public void setIdefaultstorage(Integer idefaultstorage) {
		this.idefaultstorage = idefaultstorage;
	}

	public String getCcountryname() {
		return ccountryname;
	}

	public void setCcountryname(String ccountryname) {
		this.ccountryname = ccountryname;
	}

	public Integer getIid() {
		return iid;
	}

	public void setIid(Integer iid) {
		this.iid = iid;
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