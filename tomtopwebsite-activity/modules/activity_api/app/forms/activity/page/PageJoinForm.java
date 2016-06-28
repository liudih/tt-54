package forms.activity.page;

import java.io.Serializable;
import java.util.Date;

public class PageJoinForm implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer iid;

	private String cjoiner;

	private String cjoinparam;

	private Integer iwebsiteid;

	private String vhost;

	private String ccountry;

	private String cresult;

	private Date dcreatedate;

	private Integer pageSize;

	private Integer pageNum;

	private Date startDate;

	private Date endDate;
	
	private String cuseremail;

	private Integer icemilCount;

	public Integer getIid() {
		return iid;
	}

	public void setIid(Integer iid) {
		this.iid = iid;
	}

	public String getCjoiner() {
		return cjoiner;
	}

	public void setCjoiner(String cjoiner) {
		this.cjoiner = cjoiner;
	}

	public String getCjoinparam() {
		return cjoinparam;
	}

	public void setCjoinparam(String cjoinparam) {
		this.cjoinparam = cjoinparam;
	}

	public Integer getIwebsiteid() {
		return iwebsiteid;
	}

	public void setIwebsiteid(Integer iwebsiteid) {
		this.iwebsiteid = iwebsiteid;
	}

	public String getVhost() {
		return vhost;
	}

	public void setVhost(String vhost) {
		this.vhost = vhost;
	}

	public String getCcountry() {
		return ccountry;
	}

	public void setCcountry(String ccountry) {
		this.ccountry = ccountry;
	}

	public String getCresult() {
		return cresult;
	}

	public void setCresult(String cresult) {
		this.cresult = cresult;
	}

	public Date getDcreatedate() {
		return dcreatedate;
	}

	public void setDcreatedate(Date dcreatedate) {
		this.dcreatedate = dcreatedate;
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

	public Integer getIcemilCount() {
		return icemilCount;
	}

	public void setIcemilCount(Integer icemilCount) {
		this.icemilCount = icemilCount;
	}

	public String getCuseremail() {
		return cuseremail;
	}

	public void setCuseremail(String cuseremail) {
		this.cuseremail = cuseremail;
	}

	@Override
	public String toString() {
		return "PageJoinQuery [iid=" + iid + ", cjoiner=" + cjoiner
				+ ", cjoinparam=" + cjoinparam + ", iwebsiteid=" + iwebsiteid
				+ ", vhost=" + vhost + ", ccountry=" + ccountry + ", cresult="
				+ cresult + ", dcreatedate=" + dcreatedate + ", pageSize="
				+ pageSize + ", pageNum=" + pageNum + ", startDate="
				+ startDate + ", endDate=" + endDate + ", cuseremail="
				+ cuseremail + ", icemilCount=" + icemilCount + "]";
	}
	
	

}
