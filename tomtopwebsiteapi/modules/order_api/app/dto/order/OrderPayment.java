package dto.order;

import java.io.Serializable;
import java.util.Date;

public class OrderPayment implements Serializable {
	private Integer iid;
	private String corderid;
	private String cpaymentid;
	private String cjson;
	private Date dcreatedate;

	public Integer getIid() {
		return iid;
	}

	public void setIid(Integer iid) {
		this.iid = iid;
	}

	public String getCorderid() {
		return corderid;
	}

	public void setCorderid(String corderid) {
		this.corderid = corderid;
	}

	public String getCpaymentid() {
		return cpaymentid;
	}

	public void setCpaymentid(String cpaymentid) {
		this.cpaymentid = cpaymentid;
	}

	public String getCjson() {
		return cjson;
	}

	public void setCjson(String cjson) {
		this.cjson = cjson;
	}

	public Date getDcreatedate() {
		return dcreatedate;
	}

	public void setDcreatedate(Date dcreatedate) {
		this.dcreatedate = dcreatedate;
	}

}
