package form.dropship;

import java.io.Serializable;


public class DropShipSearchForm implements Serializable {
	private static final long serialVersionUID = 1L;
	Integer pageSize;
	Integer pageNum;
	Integer status;
	String email;
	Integer level;

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
		if (email==null || "".equals(email)) {
			return null;
		}
		return email.toLowerCase();
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	@Override
	public String toString() {
		return "DropShipSearchForm [pageSize=" + pageSize + ", pageNum="
				+ pageNum + ", status=" + status + ", email=" + email
				+ ", level=" + level + "]";
	}
}
