package forms;

import java.util.Date;

import play.data.validation.Constraints.Required;

public class CustomerServiceScheduleSearchForm {
	@Required
	private Integer p;
	@Required
	private Date startDate;
	@Required
	private Date endDate;
	private Integer userId;
	private Integer pageSize;

	public Integer getP() {
		return p;
	}

	public void setP(Integer p) {
		this.p = p;
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

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	@Override
	public String toString() {
		return "CustomerServiceScheduleSearchForm [p=" + p + ", startDate="
				+ startDate + ", endDate=" + endDate + ", userId=" + userId
				+ ", pageSize=" + pageSize + "]";
	}

}
