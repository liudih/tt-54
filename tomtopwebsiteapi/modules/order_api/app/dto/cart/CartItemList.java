package dto.cart;

import java.io.Serializable;

public class CartItemList implements Serializable {
	private static final long serialVersionUID = 1L;

	private String ccartitemid;

	private Boolean bismain;

	private String clistingid;

	private Integer iqty;

	private Integer istorageid;

	public String getCcartitemid() {
		return ccartitemid;
	}

	public void setCcartitemid(String ccartitemid) {
		this.ccartitemid = ccartitemid;
	}

	public Boolean getBismain() {
		return bismain;
	}

	public void setBismain(Boolean bismain) {
		this.bismain = bismain;
	}

	public String getClistingid() {
		return clistingid;
	}

	public void setClistingid(String clistingid) {
		this.clistingid = clistingid == null ? null : clistingid.trim();
	}

	public Integer getIqty() {
		return iqty;
	}

	public void setIqty(Integer iqty) {
		this.iqty = iqty;
	}

	public Integer getIstorageid() {
		return istorageid;
	}

	public void setIstorageid(Integer istorageid) {
		this.istorageid = istorageid;
	}
}