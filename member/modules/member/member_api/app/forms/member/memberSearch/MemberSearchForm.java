package forms.member.memberSearch;

import java.io.Serializable;

public class MemberSearchForm implements Serializable {
	private static final long serialVersionUID = 1L;
	Integer pageSize;// 显示数量
	Integer pageNum;// 页数
	String email;// 邮件
	Integer siteId;// 站点
	String caccount;
	String cdotype;
	Integer status;
	String cremark;
	Integer blackUserStatus;
	String contry;
	String vhost;
	Boolean bactivated;
	Boolean bnewsletter;
	String start;
	String end;

	public Integer getPageSize() {
		return pageSize != null ? pageSize : 30;
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

	public String getEmail() {
		if (email == null || "".equals(email)) {
			return null;
		}
		return email.toLowerCase();
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Integer getSiteId() {
		return siteId;
	}

	public void setSiteId(Integer siteId) {
		this.siteId = siteId;
	}

	public String getCaccount() {
		return caccount;
	}

	public void setCaccount(String caccount) {
		this.caccount = caccount;
	}

	public String getCdotype() {
		if (cdotype == null || "".equals(cdotype)) {
			return null;
		}
		return cdotype;
	}

	public void setCdotype(String cdotype) {
		this.cdotype = cdotype;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getCremark() {
		if (cremark == null || "".equals(cremark)) {
			return null;
		}
		return cremark;
	}

	public void setCremark(String cremark) {
		this.cremark = cremark;
	}

	public Integer getBlackUserStatus() {
		return blackUserStatus;
	}

	public void setBlackUserStatus(Integer blackUserStatus) {
		this.blackUserStatus = blackUserStatus;
	}

	public String getContry() {
		if (contry == null || "".equals(contry)) {
			return null;
		}
		return contry;
	}

	public void setContry(String contry) {
		this.contry = contry;
	}

	public String getVhost() {
		if (vhost == null || "".equals(vhost)) {
			return null;
		}
		return vhost;
	}

	public void setVhost(String vhost) {
		this.vhost = vhost;
	}

	public Boolean getBactivated() {
		return bactivated;
	}

	public void setBactivated(Boolean bactivated) {
		this.bactivated = bactivated;
	}

	public Boolean getBnewsletter() {
		return bnewsletter;
	}

	public void setBnewsletter(Boolean bnewsletter) {
		this.bnewsletter = bnewsletter;
	}

	public String getStart() {
		if (start == null || "".equals(start)) {
			return null;
		}
		return start;
	}

	public void setStart(String start) {
		this.start = start;
	}

	public String getEnd() {
		if (end == null || "".equals(end)) {
			return null;
		}
		return end;
	}

	public void setEnd(String end) {
		this.end = end;
	}

}
