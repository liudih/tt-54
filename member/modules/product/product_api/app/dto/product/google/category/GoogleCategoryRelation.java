package dto.product.google.category;

import java.io.Serializable;

public class GoogleCategoryRelation implements Serializable {

	private static final long serialVersionUID = -2063282329297891453L;

	private Integer iid;
	private Integer igooglecategory;
	private Integer icategory;

	public Integer getIid() {
		return iid;
	}

	public void setIid(Integer iid) {
		this.iid = iid;
	}

	public Integer getIgooglecategory() {
		return igooglecategory;
	}

	public void setIgooglecategory(Integer igooglecategory) {
		this.igooglecategory = igooglecategory;
	}

	public Integer getIcategory() {
		return icategory;
	}

	public void setIcategory(Integer icategory) {
		this.icategory = icategory;
	}

}
