package forms.report;

public class QueryForm {

	final int defaultPageSize = 15;

	final int defaultPage = 1;

	private String id;

	private String begindate;

	private String enddate;

	private int querytype;

	private String skuornum;

	private int page = defaultPage;

	private int pageSize = defaultPageSize;

	private Integer orderStatus;

	private int comminsonStatus;

	public int getComminsonStatus() {
		return comminsonStatus;
	}

	public void setComminsonStatus(int comminsonStatus) {
		this.comminsonStatus = comminsonStatus;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Integer getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(Integer orderStatus) {
		this.orderStatus = orderStatus;
	}

	public String getBegindate() {
		return begindate;
	}

	public void setBegindate(String begindate) {
		this.begindate = "".equals(begindate) ? null : begindate;
	}

	public String getEnddate() {
		return enddate;
	}

	public void setEnddate(String enddate) {
		this.enddate = "".equals(enddate) ? null : enddate;
	}

	public int getQuerytype() {
		return querytype;
	}

	public void setQuerytype(int querytype) {
		this.querytype = querytype;
	}

	public String getSkuornum() {
		return skuornum;
	}

	public void setSkuornum(String skuornum) {
		this.skuornum = "".equals(skuornum) ? null : skuornum;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getDefaultPageSize() {
		return defaultPageSize;
	}

}
