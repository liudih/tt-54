package dto.category;

import java.io.Serializable;
import java.util.Date;

public class FilterAttributeValue implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private Integer iid;

	private Integer iattributekeyid;

	private String cvalue;

	private String ccreateuser;

	private Date dcreatedate;

	public Integer getIid() {
		return iid;
	}

	public void setIid(Integer iid) {
		this.iid = iid;
	}

	public Integer getIattributekeyid() {
		return iattributekeyid;
	}

	public void setIattributekeyid(Integer iattributekeyid) {
		this.iattributekeyid = iattributekeyid;
	}

	public String getCvalue() {
		return cvalue;
	}

	public void setCvalue(String cvalue) {
		this.cvalue = cvalue == null ? null : cvalue.trim();
	}

	public String getCcreateuser() {
		return ccreateuser;
	}

	public void setCcreateuser(String ccreateuser) {
		this.ccreateuser = ccreateuser == null ? null : ccreateuser.trim();
	}

	public Date getDcreatedate() {
		return dcreatedate;
	}

	public void setDcreatedate(Date dcreatedate) {
		this.dcreatedate = dcreatedate;
	}
}