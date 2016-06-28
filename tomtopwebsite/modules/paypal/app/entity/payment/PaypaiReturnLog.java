package entity.payment;

public class PaypaiReturnLog {

	Integer iwebsiteid;
	String corderid;
	String ccontent;
	String ctransactionid;

	public PaypaiReturnLog() {

	}

	public PaypaiReturnLog(String corderid, String ccontent) {
		this.corderid = corderid;
		this.ccontent = ccontent;
	}

	public Integer getIwebsiteid() {
		return iwebsiteid;
	}

	public void setIwebsiteid(Integer iwebsiteid) {
		this.iwebsiteid = iwebsiteid;
	}

	public String getCorderid() {
		return corderid;
	}

	public void setCorderid(String corderid) {
		this.corderid = corderid;
	}

	public String getCcontent() {
		return ccontent;
	}

	public void setCcontent(String ccontent) {
		this.ccontent = ccontent;
	}

	public String getCtransactionid() {
		return ctransactionid;
	}

	public void setCtransactionid(String ctransactionid) {
		this.ctransactionid = ctransactionid;
	}

}
