package dto;

import java.util.List;

public class OrderDetails {

	private List<OrderDetail> list;

	private int page;

	private int pageTotal;

	private int pageSize;

	private double saleTotal;

	private int totalCount;

	private double commissionTotal;

	public List<OrderDetail> getList() {
		return list;
	}

	public void setList(List<OrderDetail> list) {
		this.list = list;
	}

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

	public double getSaleTotal() {
		return saleTotal;
	}

	public void setSaleTotal(double saleTotal) {
		this.saleTotal = saleTotal;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	public double getCommissionTotal() {
		return commissionTotal;
	}

	public void setCommissionTotal(double commissionTotal) {
		this.commissionTotal = commissionTotal;
	}

}
