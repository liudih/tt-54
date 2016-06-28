package valueobjects.livechat.leave;

import java.util.Date;

public class LeaveMessage {

	private String iid;
	private String cltc;
	private String ctopic;
	private String cip;
	private String cemail;
	private String ccontent;
	private Date dcreatedate;
	private int ilanguageid;
	private String calias;

	public String getIid() {
		return iid;
	}

	public void setIid(String iid) {
		this.iid = iid;
	}

	public String getCltc() {
		return cltc;
	}

	public void setCltc(String cltc) {
		this.cltc = cltc;
	}

	public String getCtopic() {
		return ctopic;
	}

	public void setCtopic(String ctopic) {
		this.ctopic = ctopic;
	}

	public String getCip() {
		return cip;
	}

	public void setCip(String cip) {
		this.cip = cip;
	}

	public String getCemail() {
		return cemail;
	}

	public void setCemail(String cemail) {
		this.cemail = cemail;
	}

	public String getCcontent() {
		return ccontent;
	}

	public void setCcontent(String ccontent) {
		this.ccontent = ccontent;
	}

	public Date getDcreatedate() {
		return dcreatedate;
	}

	public void setDcreatedate(Date dcreatedate) {
		this.dcreatedate = dcreatedate;
	}

	public int getIlanguageid() {
		return ilanguageid;
	}

	public void setIlanguageid(int ilanguageid) {
		this.ilanguageid = ilanguageid;
	}

	public String getCalias() {
		return calias;
	}

	public void setCalias(String calias) {
		this.calias = calias;
	}

}
