package dto;

import java.util.Date;

public class CustomerServiceSchedule {
	private Integer iid;
	private Integer iuserid;
	private String cdayofweek;
	private Date dstartdate;
	private Date denddate;
	private Integer iweekofyear;
	private Date dcreatedate;
	private String userName;

	public Integer getIid() {
		return iid;
	}

	public void setIid(Integer iid) {
		this.iid = iid;
	}

	public Integer getIuserid() {
		return iuserid;
	}

	public void setIuserid(Integer iuserid) {
		this.iuserid = iuserid;
	}

	public String getCdayofweek() {
		return cdayofweek;
	}

	public void setCdayofweek(String cdayofweek) {
		this.cdayofweek = cdayofweek;
	}

	public Date getDstartdate() {
		return dstartdate;
	}

	public void setDstartdate(Date dstartdate) {
		this.dstartdate = dstartdate;
	}

	public Date getDenddate() {
		return denddate;
	}

	public void setDenddate(Date denddate) {
		this.denddate = denddate;
	}

	public Integer getIweekofyear() {
		return iweekofyear;
	}

	public void setIweekofyear(Integer iweekofyear) {
		this.iweekofyear = iweekofyear;
	}

	public Date getDcreatedate() {
		return dcreatedate;
	}

	public void setDcreatedate(Date dcreatedate) {
		this.dcreatedate = dcreatedate;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

}
