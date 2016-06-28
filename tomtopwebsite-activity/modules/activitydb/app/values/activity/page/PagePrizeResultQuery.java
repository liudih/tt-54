package values.activity.page;

import java.io.Serializable;
import java.util.Date;

public class PagePrizeResultQuery implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer iid;

	private Integer ipageid;

	private String cemail;

	private Integer iprizeid;

	private String ccountry;

	private Date dcreatedate;

	private Integer iwebsiteid;

	private String cvhost;

	private Integer pageSize;

	private Integer pageNum;

	private Date startDate;

	private Date endDate;

	private Integer totalflow;

	private Integer ijoinnum;

	private String cprizevalue;

	public Integer getIjoinnum() {
		return ijoinnum;
	}

	public void setIjoinnum(Integer ijoinnum) {
		this.ijoinnum = ijoinnum;
	}

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

	public Integer getPageSize() {
		return pageSize != null ? pageSize : 20;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public Integer getPageNum() {
		return pageNum != null ? pageNum : 1;
	}

	public void setPageNum(Integer pageNum) {
		this.pageNum = pageNum;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Integer getTotalflow() {
		return totalflow;
	}

	public void setTotalflow(Integer totalflow) {
		this.totalflow = totalflow;
	}

	public String getCprizevalue() {
		return cprizevalue;
	}

	public void setCprizevalue(String cprizevalue) {
		this.cprizevalue = cprizevalue;
	}
}
