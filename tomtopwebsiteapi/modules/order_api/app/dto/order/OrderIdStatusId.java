package dto.order;

import java.io.Serializable;

public class OrderIdStatusId implements Serializable {

	Integer iorderid;
	Integer istatus;
	String cordernumber;

	public Integer getIorderid() {
		return iorderid;
	}

	public void setIorderid(Integer iorderid) {
		this.iorderid = iorderid;
	}

	public Integer getIstatus() {
		return istatus;
	}

	public void setIstatus(Integer istatus) {
		this.istatus = istatus;
	}

	public String getCordernumber() {
		return cordernumber;
	}

	public void setCordernumber(String cordernumber) {
		this.cordernumber = cordernumber;
	}

}
