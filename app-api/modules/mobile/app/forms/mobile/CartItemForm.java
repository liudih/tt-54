package forms.mobile;

import java.io.Serializable;

public class CartItemForm implements Serializable {

	private static final long serialVersionUID = 1L;

	private String gid;

	private int qty;

	public String getGid() {
		return gid;
	}

	public void setGid(String gid) {
		this.gid = gid;
	}

	public int getQty() {
		return qty;
	}

	public void setQty(int qty) {
		this.qty = qty;
	}

}
