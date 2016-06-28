package valueobjects.product;

public class ProductPartInformation {

	private String sku;
	private String listingId;
	private Double price;
	private Double costPrice;
	private Double salePrice;
	private Integer rootCategoryId;
	private String rootCategoryName;
	private String beginDate;
	private String endDate;

	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	public String getListingId() {
		return listingId;
	}

	public void setListingId(String listingId) {
		this.listingId = listingId;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public Double getCostPrice() {
		return costPrice;
	}

	public void setCostPrice(Double costPrice) {
		this.costPrice = costPrice;
	}

	public Double getSalePrice() {
		return salePrice;
	}

	public void setSalePrice(Double salePrice) {
		this.salePrice = salePrice;
	}

	public Integer getRootCategoryId() {
		return rootCategoryId;
	}

	public void setRootCategoryId(Integer rootCategoryId) {
		this.rootCategoryId = rootCategoryId;
	}

	public String getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(String beginDate) {
		this.beginDate = beginDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getRootCategoryName() {
		return rootCategoryName;
	}

	public void setRootCategoryName(String rootCategoryName) {
		this.rootCategoryName = rootCategoryName;
	}

}
