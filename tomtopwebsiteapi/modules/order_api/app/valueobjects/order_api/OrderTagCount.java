package valueobjects.order_api;

import java.io.Serializable;

public class OrderTagCount implements Serializable {

	int iorderid;
	int icount;

	public int getIorderid() {
		return iorderid;
	}

	public void setIorderid(int iorderid) {
		this.iorderid = iorderid;
	}

	public int getIcount() {
		return icount;
	}

	public void setIcount(int icount) {
		this.icount = icount;
	}

}
