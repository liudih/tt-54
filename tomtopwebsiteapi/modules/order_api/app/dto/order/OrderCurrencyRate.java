package dto.order;

import java.io.Serializable;
import java.util.Date;

public class OrderCurrencyRate implements Serializable {

	private Integer iid;
	private String ccurrency;
	private String cordernumber;
	private Double frate;
	private Date dcreatedate;

	public Integer getIid() {
		return iid;
	}

	public void setIid(Integer iid) {
		this.iid = iid;
	}

	public String getCcurrency() {
		return ccurrency;
	}

	public void setCcurrency(String ccurrency) {
		this.ccurrency = ccurrency;
	}

	public String getCordernumber() {
		return cordernumber;
	}

	public void setCordernumber(String cordernumber) {
		this.cordernumber = cordernumber;
	}

	public Double getFrate() {
		return frate;
	}

	public void setFrate(Double frate) {
		this.frate = frate;
	}

	public Date getDcreatedate() {
		return dcreatedate;
	}

	public void setDcreatedate(Date dcreatedate) {
		this.dcreatedate = dcreatedate;
	}

	@Override
	public String toString() {
		return "OrderCurrencyRate [iid=" + iid + ", ccurrency=" + ccurrency
				+ ", cordernumber=" + cordernumber + ", frate=" + frate
				+ ", dcreatedate=" + dcreatedate + "]";
	}

}
