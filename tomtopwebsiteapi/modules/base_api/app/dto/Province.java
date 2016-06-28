package dto;

import java.io.Serializable;
import java.util.Date;

public class Province implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer iid;
	private Integer icountryid;
	private String cname;
	private String cshortname;
	private String ccreateuser;
	Date dcreatedate;

	public Integer getIid() {
		return iid;
	}

	public Integer getIcountryid() {
		return icountryid;
	}

	public String getCname() {
		return cname;
	}

	public String getCshortname() {
		return cshortname;
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

	public void setIcountryid(Integer icountryid) {
		this.icountryid = icountryid;
	}

	public void setCname(String cname) {
		this.cname = cname;
	}

	public void setCshortname(String cshortname) {
		this.cshortname = cshortname;
	}

	public void setCcreateuser(String ccreateuser) {
		this.ccreateuser = ccreateuser;
	}

	public void setDcreatedate(Date dcreatedate) {
		this.dcreatedate = dcreatedate;
	}

}
