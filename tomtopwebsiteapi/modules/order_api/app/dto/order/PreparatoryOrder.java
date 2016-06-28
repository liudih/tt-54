package dto.order;

import java.util.Date;

public class PreparatoryOrder {
	private Integer iid;
	private Integer iwebsiteid;
	private Integer istorageid;
	private String ccartid;
	private String cmemberemail;
	private Date dcreatedate;
	private Integer ino;
	private String ccurrency;

	public Integer getIid() {
		return iid;
	}

	public void setIid(Integer iid) {
		this.iid = iid;
	}

	public Integer getIwebsiteid() {
		return iwebsiteid;
	}

	public void setIwebsiteid(Integer iwebsiteid) {
		this.iwebsiteid = iwebsiteid;
	}

	public Integer getIstorageid() {
		return istorageid;
	}

	public void setIstorageid(Integer istorageid) {
		this.istorageid = istorageid;
	}

	public String getCcartid() {
		return ccartid;
	}

	public void setCcartid(String ccartid) {
		this.ccartid = ccartid;
	}

	public String getCmemberemail() {
		return cmemberemail;
	}

	public void setCmemberemail(String cmemberemail) {
		this.cmemberemail = cmemberemail;
	}

	public Date getDcreatedate() {
		return dcreatedate;
	}

	public void setDcreatedate(Date dcreatedate) {
		this.dcreatedate = dcreatedate;
	}

	public Integer getIno() {
		return ino;
	}

	public void setIno(Integer ino) {
		this.ino = ino;
	}

	public String getCcurrency() {
		return ccurrency;
	}

	public void setCcurrency(String ccurrency) {
		this.ccurrency = ccurrency;
	}

}
