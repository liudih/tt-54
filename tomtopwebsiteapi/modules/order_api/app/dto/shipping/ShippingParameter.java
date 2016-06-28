package dto.shipping;

import java.io.Serializable;

public class ShippingParameter implements Serializable {
	private String ckey;
	private String cjsonvalue;

	public String getCkey() {
		return ckey;
	}

	public void setCkey(String ckey) {
		this.ckey = ckey;
	}

	public String getCjsonvalue() {
		return cjsonvalue;
	}

	public void setCjsonvalue(String cjsonvalue) {
		this.cjsonvalue = cjsonvalue;
	}

}
