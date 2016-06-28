package dto.product;

import java.io.Serializable;

public class ProductExplainType implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	Integer iid;

	String ctype;

	public Integer getIid() {
		return iid;
	}

	public void setIid(Integer iid) {
		this.iid = iid;
	}

	public String getCtype() {
		return ctype;
	}

	public void setCtype(String ctype) {
		this.ctype = ctype;
	}

}
