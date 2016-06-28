package dto.cart;

import java.io.Serializable;

public class ExtraLine implements Serializable {
	private static final long serialVersionUID = 1L;
	private String cid;
	private String ccartbaseid;
	private String cpluginid;
	private String cpayload;
	private String ccreateuser;

	public ExtraLine() {
	};

	public ExtraLine(String cid, String ccartbaseid, String cpluginid,
			String cpayload) {
		this.cid = cid;
		this.ccartbaseid = ccartbaseid;
		this.cpluginid = cpluginid;
		this.cpayload = cpayload;
	}

	public String getCid() {
		return cid;
	}

	public void setCid(String cid) {
		this.cid = cid;
	}

	public String getCcartbaseid() {
		return ccartbaseid;
	}

	public void setCcartbaseid(String ccartbaseid) {
		this.ccartbaseid = ccartbaseid;
	}

	public String getCpluginid() {
		return cpluginid;
	}

	public void setCpluginid(String cpluginid) {
		this.cpluginid = cpluginid;
	}

	public String getCpayload() {
		return cpayload;
	}

	public void setCpayload(String cpayload) {
		this.cpayload = cpayload;
	}

	public String getCcreateuser() {
		return ccreateuser;
	}

	public void setCcreateuser(String ccreateuser) {
		this.ccreateuser = ccreateuser;
	}

}
