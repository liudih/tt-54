package dto.product.google.category;

import java.io.Serializable;

public class GoogleRelationForm implements Serializable {

	private static final long serialVersionUID = 2942855809239482685L;

	private Integer googlecategory;
	private String path;
	private String gname;
	private Integer category;
	private String wname;

	public Integer getGooglecategory() {
		return googlecategory;
	}

	public void setGooglecategory(Integer googlecategory) {
		this.googlecategory = googlecategory;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getGname() {
		return gname;
	}

	public void setGname(String gname) {
		this.gname = gname;
	}

	public Integer getCategory() {
		return category;
	}

	public void setCategory(Integer category) {
		this.category = category;
	}

	public String getWname() {
		return wname;
	}

	public void setWname(String wname) {
		this.wname = wname;
	}

}
