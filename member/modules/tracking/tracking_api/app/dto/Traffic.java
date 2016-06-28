package dto;

import java.util.List;

public class Traffic {

	private int page;

	private int pageTotal;

	private int pageSize;

	private int clickTotal;

	private int unclickTotal;

	private int totalCount;

	private List<VisitLog> list;

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getPageTotal() {
		return pageTotal;
	}

	public void setPageTotal(int pageTotal) {
		this.pageTotal = pageTotal;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getClickTotal() {
		return clickTotal;
	}

	public void setClickTotal(int clickTotal) {
		this.clickTotal = clickTotal;
	}

	public int getUnclickTotal() {
		return unclickTotal;
	}

	public void setUnclickTotal(int unclickTotal) {
		this.unclickTotal = unclickTotal;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	public List<VisitLog> getList() {
		return list;
	}

	public void setList(List<VisitLog> list) {
		this.list = list;
	}

}
