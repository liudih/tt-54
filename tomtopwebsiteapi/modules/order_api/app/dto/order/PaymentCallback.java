package dto.order;

import java.io.Serializable;

public class PaymentCallback implements Serializable {
	private Integer iid;
	private String cordernumber;
	private String ccontent;
	private String cpaymentid;
	private String dcreatedate;
	private String cresponse;
	private Integer iwebsiteid;

	public Integer getIid() {
		return iid;
	}

	public void setIid(Integer iid) {
		this.iid = iid;
	}

	public String getCordernumber() {
		return cordernumber;
	}

	public void setCordernumber(String cordernumber) {
		this.cordernumber = cordernumber;
	}

	public String getCcontent() {
		return ccontent;
	}

	public void setCcontent(String ccontent) {
		this.ccontent = ccontent;
	}

	public String getCpaymentid() {
		return cpaymentid;
	}

	public void setCpaymentid(String cpaymentid) {
		this.cpaymentid = cpaymentid;
	}

	public String getDcreatedate() {
		return dcreatedate;
	}

	public void setDcreatedate(String dcreatedate) {
		this.dcreatedate = dcreatedate;
	}

	public String getCresponse() {
		return cresponse;
	}

	public void setCresponse(String cresponse) {
		this.cresponse = cresponse;
	}

	public Integer getIwebsiteid() {
		return iwebsiteid;
	}

	public void setIwebsiteid(Integer iwebsiteid) {
		this.iwebsiteid = iwebsiteid;
	}

}
