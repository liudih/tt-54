package valueobjects.order_api;

import java.io.Serializable;

public class OrderTag implements Serializable {

	int iorderid;

	String ctag;

	public int getIorderid() {
		return iorderid;
	}

	public void setIorderid(int iorderid) {
		this.iorderid = iorderid;
	}

	public String getCtag() {
		return ctag;
	}

	public void setCtag(String ctag) {
		this.ctag = ctag;
	}

}
