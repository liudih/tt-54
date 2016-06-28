package forms;

public class OrderReportForm {

	private String aid;

	private int salerid;

	private String skuornum;

	private String enddate;

	private String begindate;

	private int page;

	private int pageSize;

	private int status;
	
	private Integer website;

	public String getAid() {
		return aid;
	}

	public void setAid(String aid) {
		this.aid = aid;
	}

	public int getSalerid() {
		return salerid;
	}

	public void setSalerid(int salerid) {
		this.salerid = salerid;
	}

	public String getSkuornum() {
		return skuornum;
	}

	public void setSkuornum(String skuornum) {
		this.skuornum = skuornum;
	}

	public String getEnddate() {
		return enddate;
	}

	public void setEnddate(String enddate) {
		this.enddate = enddate;
	}

	public String getBegindate() {
		return begindate;
	}

	public void setBegindate(String begindate) {
		this.begindate = begindate;
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

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Integer getWebsite(){
		return website;
	}
	
	public void setWebsite(Integer website){
		this.website = website;
	}
}
