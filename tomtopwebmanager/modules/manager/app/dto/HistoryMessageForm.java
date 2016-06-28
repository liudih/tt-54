package dto;

import java.util.Date;

public class HistoryMessageForm {
	private String customerName;
	private String customerServiceName;
	private String keyword;
	private Date startDate;
	private Date endDate;

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getCustomerServiceName() {
		return customerServiceName;
	}

	public void setCustomerServiceName(String customerServiceName) {
		this.customerServiceName = customerServiceName;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
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

	@Override
	public String toString() {
		return "HistoryMessageForm [customerName=" + customerName
				+ ", customerServiceName=" + customerServiceName + ", keyword="
				+ keyword + ", startDate=" + startDate + ", endDate=" + endDate
				+ "]";
	}

}
