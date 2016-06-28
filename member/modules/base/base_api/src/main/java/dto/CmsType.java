package dto;

import java.io.Serializable;

public class CmsType implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String value;

	private String cname;

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getCname() {
		return cname;
	}

	public void setCname(String cname) {
		this.cname = cname;
	}

	public CmsType(String value, String cname) {
		super();
		this.value = value;
		this.cname = cname;
	}
	 
}