package dto;

public class TopBrowseAndSaleCount {

	private String listingid;
	private Integer salecount;
	private Integer rootCategoryId;
	private Integer browsecount;
	private String sku;

	public TopBrowseAndSaleCount() {
	}

	public TopBrowseAndSaleCount(String listingid, Integer salecount,
			Integer rootCategoryId, Integer browsecount, String sku) {
		super();
		this.listingid = listingid;
		this.salecount = salecount;
		this.rootCategoryId = rootCategoryId;
		this.browsecount = browsecount;
		this.sku = sku;
	}

	public String getListingid() {
		return listingid;
	}

	public Integer getSalecount() {
		return salecount;
	}

	public Integer getRootCategoryId() {
		return rootCategoryId;
	}

	public Integer getBrowsecount() {
		return browsecount;
	}

	public void setListingid(String listingid) {
		this.listingid = listingid;
	}

	public void setSalecount(Integer salecount) {
		this.salecount = salecount;
	}

	public void setRootCategoryId(Integer rootCategoryId) {
		this.rootCategoryId = rootCategoryId;
	}

	public void setBrowsecount(Integer browsecount) {
		this.browsecount = browsecount;
	}

	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

}
