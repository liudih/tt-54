package valueobjects.product.sort;

public class ProductSort {
	private String listingId;
	private Integer viewCount;
	private Integer commentCount;
	private Double saleCount;
	
	
	public ProductSort() {
		super();
	}

	public ProductSort(String listingId, Integer viewCount,
			Integer commentCount, Double saleCount) {
		super();
		this.listingId = listingId;
		this.viewCount = viewCount;
		this.commentCount = commentCount;
		this.saleCount = saleCount;
	}
	
	public Integer getViewCount() {
		return viewCount;
	}
	public void setViewCount(Integer viewCount) {
		this.viewCount = viewCount;
	}
	public Integer getCommentCount() {
		return commentCount;
	}
	public void setCommentCount(Integer commentCount) {
		this.commentCount = commentCount;
	}
	public Double getSaleCount() {
		return saleCount;
	}
	public void setSaleCount(Double saleCount) {
		this.saleCount = saleCount;
	}
	public String getListingId() {
		return listingId;
	}
	public void setListingId(String listingId) {
		this.listingId = listingId;
	}
	
	@Override
	public String toString() {
		return "ProductSort [listingId=" + listingId + ", viewCount="
				+ viewCount + ", commentCount=" + commentCount + ", saleCount="
				+ saleCount + "]";
	}
	
	
}
