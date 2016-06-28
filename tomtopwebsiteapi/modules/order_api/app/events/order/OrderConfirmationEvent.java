package events.order;

import java.io.Serializable;

public class OrderConfirmationEvent implements Serializable {
	String cemail;
	Integer iorderid;
	String icartid;

	public OrderConfirmationEvent(String cemail, Integer iorderid,
			String icartid) {
		this.cemail = cemail;
		this.iorderid = iorderid;
		this.icartid = icartid;
	}

	public String getCemail() {
		return cemail;
	}

	public void setCemail(String cemail) {
		this.cemail = cemail;
	}

	public Integer getIorderid() {
		return iorderid;
	}

	public void setIorderid(Integer iorderid) {
		this.iorderid = iorderid;
	}

	public String getIcartid() {
		return icartid;
	}

	public void setIcartid(String icartid) {
		this.icartid = icartid;
	}

}
