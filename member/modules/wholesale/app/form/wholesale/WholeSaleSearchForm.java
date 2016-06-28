package form.wholesale;

import services.base.utils.StringUtils;

public class WholeSaleSearchForm {
	Integer pageSize;
	Integer pageNum;
	Integer status;
	String email;
	Integer websiteId;

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

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getEmail() {
		if (StringUtils.isEmpty(email)) {
			return null;
		}
		return email.toLowerCase();
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	public Integer getWebsiteId() {
		return websiteId;
	}

	public void setWebsiteId(Integer websiteId) {
		this.websiteId = websiteId;
	}

	@Override
	public String toString() {
		return "WholeSaleSearchForm [pageSize=" + pageSize + ", pageNum="
				+ pageNum + ", status=" + status + ", email=" + email
				+ ", websiteId=" + websiteId + "]";
	}
}
