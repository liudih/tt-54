package dto;

import java.util.Date;

public class CustomerServiceScoreDTO {
	private Integer iid;
	private String csessionid;
	private String ccustomerserviceltc;
	private String ccustomerltc;
	private String ccustomerservicealias;
	private String ccustomeralias;
	private String ccontent;
	private String ctopic;
	private String ctype;
	private Integer iscore;
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

	public String getCcustomerserviceltc() {
		return ccustomerserviceltc;
	}

	public void setCcustomerserviceltc(String ccustomerserviceltc) {
		this.ccustomerserviceltc = ccustomerserviceltc;
	}

	public String getCcustomerltc() {
		return ccustomerltc;
	}

	public void setCcustomerltc(String ccustomerltc) {
		this.ccustomerltc = ccustomerltc;
	}

	public String getCcustomerservicealias() {
		return ccustomerservicealias;
	}

	public void setCcustomerservicealias(String ccustomerservicealias) {
		this.ccustomerservicealias = ccustomerservicealias;
	}

	public String getCcustomeralias() {
		return ccustomeralias;
	}

	public void setCcustomeralias(String ccustomeralias) {
		this.ccustomeralias = ccustomeralias;
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

	public String getCtype() {
		return ctype;
	}

	public void setCtype(String ctype) {
		this.ctype = ctype;
	}

	public Integer getIscore() {
		return iscore;
	}

	public void setIscore(Integer iscore) {
		this.iscore = iscore;
	}

	public Date getDcreatedate() {
		return dcreatedate;
	}

	public void setDcreatedate(Date dcreatedate) {
		this.dcreatedate = dcreatedate;
	}
}
