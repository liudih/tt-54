package dto;

import java.io.Serializable;

public class ProductEntityMapLite implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private String ckey;

	private String cvalue;

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
}
