package valueobjects.search;

import java.util.Date;

public class ProductTypeInfo {
	String type;
	Date tagDate;

	public ProductTypeInfo(String type, Date tagDate) {
		super();
		this.type = type;
		this.tagDate = tagDate;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Date getTagDate() {
		return tagDate;
	}

	public void setTagDate(Date tagDate) {
		this.tagDate = tagDate;
	}
}
