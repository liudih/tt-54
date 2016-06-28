package entity.manager;

import java.util.Date;

public class LeaveMsgInfo {
	private String iid;
	private String cltc;
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
	private String topicType;
	private Integer ipretreatmentid;
	private Integer ireplyuserid;
	private String creplycontent;
	private Date dreplydate;

	public String getTopicType() {
		return topicType;
	}

	public void setTopicType(String topicType) {
		this.topicType = topicType;
	}

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

	public Integer getIpretreatmentid() {
		return ipretreatmentid;
	}

	public void setIpretreatmentid(Integer ipretreatmentid) {
		this.ipretreatmentid = ipretreatmentid;
	}

	public Integer getIreplyuserid() {
		return ireplyuserid;
	}

	public void setIreplyuserid(Integer ireplyuserid) {
		this.ireplyuserid = ireplyuserid;
	}

	public String getCreplycontent() {
		return creplycontent;
	}

	public void setCreplycontent(String creplycontent) {
		this.creplycontent = creplycontent;
	}

	public Date getDreplydate() {
		return dreplydate;
	}

	public void setDreplydate(Date dreplydate) {
		this.dreplydate = dreplydate;
	}

}
