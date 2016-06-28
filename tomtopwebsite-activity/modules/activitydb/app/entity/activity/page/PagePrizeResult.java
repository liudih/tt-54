package entity.activity.page;

import java.util.Date;

/**
 * 奖品统计报表
 * 
 * @author Guozy
 *
 */
public class PagePrizeResult {

	private Integer iid;

	private Integer ipageid;

	private String cemail;

	private Integer iprizeid;

	private String ccountry;

	private Date dcreatedate;

	private Integer iwebsiteid;

	private String cvhost;

	private String cprizevalue;

	public Integer getIid() {
		return iid;
	}

	public void setIid(Integer iid) {
		this.iid = iid;
	}

	public Integer getIpageid() {
		return ipageid;
	}

	public void setIpageid(Integer ipageid) {
		this.ipageid = ipageid;
	}

	public String getCemail() {
		return cemail;
	}

	public void setCemail(String cemail) {
		this.cemail = cemail;
	}

	public Integer getIprizeid() {
		return iprizeid;
	}

	public void setIprizeid(Integer iprizeid) {
		this.iprizeid = iprizeid;
	}

	public String getCcountry() {
		return ccountry;
	}

	public void setCcountry(String ccountry) {
		this.ccountry = ccountry;
	}

	public Date getDcreatedate() {
		return dcreatedate;
	}

	public void setDcreatedate(Date dcreatedate) {
		this.dcreatedate = dcreatedate;
	}

	public Integer getIwebsiteid() {
		return iwebsiteid;
	}

	public void setIwebsiteid(Integer iwebsiteid) {
		this.iwebsiteid = iwebsiteid;
	}

	public String getCvhost() {
		return cvhost;
	}

	public void setCvhost(String cvhost) {
		this.cvhost = cvhost;
	}

	@Override
	public String toString() {
		return "PagePrizeResult [iid=" + iid + ", ipageid=" + ipageid
				+ ", cemail=" + cemail + ", iprizeid=" + iprizeid
				+ ", ccountry=" + ccountry + ", dcreatedate=" + dcreatedate
				+ ", iwebsiteid=" + iwebsiteid + ", cvhost=" + cvhost + "]";
	}

	public String getCprizevalue() {
		return cprizevalue;
	}

	public void setCprizevalue(String cprizevalue) {
		this.cprizevalue = cprizevalue;
	}

}