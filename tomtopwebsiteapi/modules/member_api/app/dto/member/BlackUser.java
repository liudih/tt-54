package dto.member;

import java.util.Date;

/**
 * 黑名单会员实体类对象
 * 
 * @author guozy
 *
 */
public class BlackUser {

	// 黑名单id
	private Integer iid;
	// 站点id
	private Integer iwebsiteid;
	// 添加黑名单的理由
	private String creason;
	// 用户的email
	private String cemail;
	// 创建黑名单的时间
	private Date dcreatedate;
	// 是否是黑用户
	private Integer istatus;

	public Integer getIid() {
		return iid;
	}

	public void setIid(Integer iid) {
		this.iid = iid;
	}

	public Integer getIwebsiteid() {
		return iwebsiteid;
	}

	public void setIwebsiteid(Integer iwebsiteid) {
		this.iwebsiteid = iwebsiteid;
	}

	public String getCreason() {
		return creason;
	}

	public void setCreason(String creason) {
		this.creason = creason;
	}

	public String getCemail() {
		return cemail;
	}

	public void setCemail(String cemail) {
		this.cemail = cemail;
	}

	public Date getDcreatedate() {
		return dcreatedate;
	}

	public void setDcreatedate(Date dcreatedate) {
		this.dcreatedate = dcreatedate;
	}

	public Integer getIstatus() {
		return istatus;
	}

	public void setIstatus(Integer istatus) {
		this.istatus = istatus;
	}

	@Override
	public String toString() {
		return "BlackUser [iid=" + iid + ", iwebsiteid=" + iwebsiteid
				+ ", creason=" + creason + ", cemail=" + cemail
				+ ", dcreatedate=" + dcreatedate + ", istatus=" + istatus + "]";
	}

}
