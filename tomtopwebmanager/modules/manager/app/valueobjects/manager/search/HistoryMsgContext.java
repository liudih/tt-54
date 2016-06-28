package valueobjects.manager.search;

import java.util.Date;

public class HistoryMsgContext {
	private String customerName;
	private String customerServiceName;
	private String keyword;
	private Date startDate;
	private Date endDate;
	private int pageSize = 10;
	private int page = 1;
	private String customerLtc;

	public String getCustomerLtc() {
		return customerLtc;
	}

	public void setCustomerLtc(String customerLtc) {
		this.customerLtc = customerLtc;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

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
}
