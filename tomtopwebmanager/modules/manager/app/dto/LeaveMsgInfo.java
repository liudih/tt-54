package dto;

import java.util.Date;

public class LeaveMsgInfo {
	private String iid;
	private String ctopic;
	private String cip;
	private String cemail;
	private String ccontent;
	private Date dcreatedate;
	private int ilanguageid;
	private String chandler;
	private Date dhandledate;
	private boolean bishandle;
	private String calias;
	private String languageName;
	private String skillName;
	
	private String replyUserName;
	private String replyContent;
	private Date replyDate;
	private String pretreatmentUserName;
	
	public String getSkillName() {
		return skillName;
	}

	public void setSkillName(String skillName) {
		this.skillName = skillName;
	}

	public String getLanguageName() {
		return languageName;
	}

	public void setLanguageName(String languageName) {
		this.languageName = languageName;
	}

	public String getIid() {
		return iid;
	}

	public void setIid(String iid) {
		this.iid = iid;
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

	public String getChandler() {
		return chandler;
	}

	public void setChandler(String chandler) {
		this.chandler = chandler;
	}

	public Date getDhandledate() {
		return dhandledate;
	}

	public void setDhandledate(Date dhandledate) {
		this.dhandledate = dhandledate;
	}

	public boolean isBishandle() {
		return bishandle;
	}

	public void setBishandle(boolean bishandle) {
		this.bishandle = bishandle;
	}

	public String getCalias() {
		return calias;
	}

	public void setCalias(String calias) {
		this.calias = calias;
	}

	public String getReplyUserName() {
		return replyUserName;
	}

	public void setReplyUserName(String replyUserName) {
		this.replyUserName = replyUserName;
	}

	public String getReplyContent() {
		return replyContent;
	}

	public void setReplyContent(String replyContent) {
		this.replyContent = replyContent;
	}

	public Date getReplyDate() {
		return replyDate;
	}

	public void setReplyDate(Date replyDate) {
		this.replyDate = replyDate;
	}

	public String getPretreatmentUserName() {
		return pretreatmentUserName;
	}

	public void setPretreatmentUserName(String pretreatmentUserName) {
		this.pretreatmentUserName = pretreatmentUserName;
	}

}
