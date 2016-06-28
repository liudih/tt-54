package entity.manager;

import java.util.Date;

public class LivechatMsgInfo {
	private Integer iid;
	private String csessionid;
	private String cfromltc;
	private String ctoltc;
	private String cfromalias;
	private String ctoalias;
	private String ccontent;
	private String ctopic;
	private Date dcreatedate;

	public Integer getIid() {
		return iid;
	}

	public void setIid(Integer iid) {
		this.iid = iid;
	}

	public String getCsessionid() {
		return csessionid;
	}

	public void setCsessionid(String csessionid) {
		this.csessionid = csessionid;
	}

	public String getCfromltc() {
		return cfromltc;
	}

	public void setCfromltc(String cfromltc) {
		this.cfromltc = cfromltc;
	}

	public String getCtoltc() {
		return ctoltc;
	}

	public void setCtoltc(String ctoltc) {
		this.ctoltc = ctoltc;
	}

	public String getCfromalias() {
		return cfromalias;
	}

	public void setCfromalias(String cfromalias) {
		this.cfromalias = cfromalias;
	}

	public String getCtoalias() {
		return ctoalias;
	}

	public void setCtoalias(String ctoalias) {
		this.ctoalias = ctoalias;
	}

	public String getCcontent() {
		return ccontent;
	}

	public void setCcontent(String ccontent) {
		this.ccontent = ccontent;
	}

	public String getCtopic() {
		return ctopic;
	}

	public void setCtopic(String ctopic) {
		this.ctopic = ctopic;
	}

	public Date getDcreatedate() {
		return dcreatedate;
	}

	public void setDcreatedate(Date dcreatedate) {
		this.dcreatedate = dcreatedate;
	}

}
