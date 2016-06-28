package dto;

import java.io.Serializable;
import java.util.Date;

/**
 * 仓库可送达地区
 * 
 * @author Administrator
 *
 */
public class StorageArrival  implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Integer iid;
	// 仓库ID
	Integer istorageid;

	// 可送达地区名
	String ccarrivalcountry;

	String ccreateuser;

	Date dcreatedate;

	
	public Integer getIstorageid() {
		return istorageid;
	}

	public void setIstorageid(Integer istorageid) {
		this.istorageid = istorageid;
	}

	public String getCcarrivalcountry() {
		return ccarrivalcountry;
	}

	public void setCcarrivalcountry(String ccarrivalcountry) {
		this.ccarrivalcountry = ccarrivalcountry;
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