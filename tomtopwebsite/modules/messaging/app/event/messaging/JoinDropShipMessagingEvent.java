package event.messaging;

public class JoinDropShipMessagingEvent {

	final String toemail;
	final Integer istatus;
	final Integer iwebsiteid;
	final Integer ilanguagid;
	final Integer isendiid;

	public JoinDropShipMessagingEvent(String toemail, Integer istatus,
			Integer iwebsiteid, Integer ilanguagid, Integer isendiid) {
		super();
		this.toemail = toemail;
		this.istatus = istatus;
		this.iwebsiteid = iwebsiteid;
		this.ilanguagid = ilanguagid;
		this.isendiid = isendiid;
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

	public Integer getIlanguagid() {
		return ilanguagid;
	}

	public Integer getIsendiid() {
		return isendiid;
	}
	
}
