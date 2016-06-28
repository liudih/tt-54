package dto.product.google.category;

import java.io.Serializable;

public class AttributeKey implements Serializable {

	private static final long serialVersionUID = -7153617459636919618L;

	private Integer iid;
	private String ckeyname;

	public Integer getIid() {
		return iid;
	}

	public void setIid(Integer iid) {
		this.iid = iid;
	}

	public String getCkeyname() {
		return ckeyname;
	}

	public void setCkeyname(String ckeyname) {
		this.ckeyname = ckeyname;
	}

}
