package dto.member;

import java.io.Serializable;
import java.util.Date;

public class MemberLoginHistory implements Serializable {
	private static final long serialVersionUID = 1L;
	Date dtimestamp;
	String cemail;
	int iwebsiteid;
	String cltc;
	String cstc;
	String cclientip;

	public Date getDtimestamp() {
		return dtimestamp;
	}

	public void setDtimestamp(Date dtimestamp) {
		this.dtimestamp = dtimestamp;
	}

	public String getCemail() {
		return cemail;
	}

	public void setCemail(String cemail) {
		this.cemail = cemail;
	}

	public int getIwebsiteid() {
		return iwebsiteid;
	}

	public void setIwebsiteid(int iwebsiteid) {
		this.iwebsiteid = iwebsiteid;
	}

	public String getCltc() {
		return cltc;
	}

	public void setCltc(String cltc) {
		this.cltc = cltc;
	}

	public String getCstc() {
		return cstc;
	}

	public void setCstc(String cstc) {
		this.cstc = cstc;
	}

	public String getCclientip() {
		return cclientip;
	}

	public void setCclientip(String cclientip) {
		this.cclientip = cclientip;
	}

}
