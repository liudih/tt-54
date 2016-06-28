package forms.manager.api;

import play.data.validation.Constraints.Required;

public class VisitLogForm {
	
	@Required
	private Integer page;
	private Integer pageSize;
	@Required
	private String startDate;
	@Required
	private String endDate;
	public Integer getPage() {
		return page==null ? 1 : page;
	}
	public void setPage(Integer page) {
		this.page = page;
	}
	public Integer getPageSize() {
		return pageSize==null ? 30 : pageSize;
	}
	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
}
