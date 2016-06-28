package dto.product.google.category;

import java.io.Serializable;

public class AttributeValue implements Serializable {

	private static final long serialVersionUID = -8988773388399394541L;

	private Integer iid;
	private String cvaluename;

	public Integer getIid() {
		return iid;
	}

	public void setIid(Integer iid) {
		this.iid = iid;
	}

	public String getCvaluename() {
		return cvaluename;
	}

	public void setCvaluename(String cvaluename) {
		this.cvaluename = cvaluename;
	}

}
