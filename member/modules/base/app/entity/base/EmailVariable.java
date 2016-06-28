package entity.base;

import java.util.Date;

import services.base.utils.DateFormatUtils;

public class EmailVariable {

	private Integer iid;

	private String ctype;

	private String cname;

	private String cremark;

	private String ccreateuser;

	private Date dcreatedate;

	public Integer getIid() {
		return iid;
	}

	public String getCtype() {
		return ctype;
	}

	public String getCname() {
		return cname;
	}

	public String getCremark() {
		return cremark;
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

	public void setCtype(String ctype) {
		this.ctype = ctype;
	}

	public void setCname(String cname) {
		this.cname = cname;
	}

	public void setCremark(String cremark) {
		this.cremark = cremark;
	}

	public void setCcreateuser(String ccreateuser) {
		this.ccreateuser = ccreateuser;
	}

	public void setDcreatedate(Date dcreatedate) {
		this.dcreatedate = dcreatedate;
	}

	public String getDate() {
		return DateFormatUtils.getDateTimeYYYYMMDD(dcreatedate);
	}

}
