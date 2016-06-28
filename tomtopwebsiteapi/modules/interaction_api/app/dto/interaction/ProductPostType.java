package dto.interaction;

import java.io.Serializable;
import java.util.Date;

public class ProductPostType implements Serializable {
	private static final long serialVersionUID = 1L;

	private Integer iid;
	private String ccode;
	private Date dcreatedate;

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

	public Date getDcreatedate() {
		return dcreatedate;
	}

	public void setDcreatedate(Date dcreatedate) {
		this.dcreatedate = dcreatedate;
	}
}
