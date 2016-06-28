package forms;

public class CategoryLabelSearchForm {
	Integer pageSize;
	
	Integer pageNum;
	
	Integer siteId;
	
	String type;

	public Integer getPageSize() {
		return pageSize != null ? pageSize : 10;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public Integer getPageNum() {
		return pageNum != null ? pageNum : 1;
	}

	public void setPageNum(Integer pageNum) {
		this.pageNum = pageNum;
	}

	public Integer getSiteId() {
		return siteId;
	}

	public void setSiteId(Integer siteId) {
		this.siteId = siteId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return "CategoryLabelSearchForm [pageSize=" + pageSize + ", pageNum="
				+ pageNum + ", siteId=" + siteId + ", type=" + type + "]";
	}
}
