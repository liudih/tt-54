package dto;

import java.io.Serializable;
import java.util.Date;

public class CartItemHistoryLite implements Serializable {
	private static final long serialVersionUID = 1L;
	private Integer cartitemid;
	
	private String clistingid;
	
	private Date dadddate;

	public Integer getCartitemid() {
		return cartitemid;
	}

	public void setCartitemid(Integer cartitemid) {
		this.cartitemid = cartitemid;
	}

	public String getClistingid() {
		return clistingid;
	}

	public void setClistingid(String clistingid) {
		this.clistingid = clistingid;
	}

	public Date getDadddate() {
		return dadddate;
	}

	public void setDadddate(Date dadddate) {
		this.dadddate = dadddate;
	}
}
