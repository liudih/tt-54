package dto.category;

import java.io.Serializable;
import java.util.Date;

public class AttributeKey implements Serializable {

	private static final long serialVersionUID = 1L;

	Integer ikeyid;
	String ccreateuser;
	Date dcreatedate;

	public Integer getIkeyid() {
		return ikeyid;
	}

	public void setIkeyid(Integer ikeyid) {
		this.ikeyid = ikeyid;
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
