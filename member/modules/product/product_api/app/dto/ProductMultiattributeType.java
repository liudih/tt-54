package dto;

import java.io.Serializable;

public class ProductMultiattributeType implements Serializable {
	private static final long serialVersionUID = 1L;
	private String ckey;
	private String cvalue;
	private String clistingid;

	public String getCkey() {
		return ckey;
	}

	public void setCkey(String ckey) {
		this.ckey = ckey;
	}

	public String getCvalue() {
		return cvalue;
	}

	public void setCvalue(String cvalue) {
		this.cvalue = cvalue;
	}

	public String getClistingid() {
		return clistingid;
	}

	public void setClistingid(String clistingid) {
		this.clistingid = clistingid;
	}

}
