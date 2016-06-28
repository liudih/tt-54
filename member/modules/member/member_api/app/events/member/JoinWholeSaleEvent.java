package events.member;

import java.io.Serializable;

public class JoinWholeSaleEvent implements Serializable {
	
	private static final long serialVersionUID = 1L;
	final String toemail;
	final Integer istatus;
	final Integer iwebsiteid;
	final Integer ilanguageid;

	public JoinWholeSaleEvent(String toemail, Integer istatus,
			Integer iwebsiteid, Integer ilanguageid) {
		super();
		this.toemail = toemail;
		this.istatus = istatus;
		this.iwebsiteid = iwebsiteid;
		this.ilanguageid = ilanguageid;
	}

	public String getToemail() {
		return toemail;
	}

	public Integer getIstatus() {
		return istatus;
	}

	public Integer getIwebsiteid() {
		return iwebsiteid;
	}

	public Integer getIlanguageid() {
		return ilanguageid;
	}
	
}
