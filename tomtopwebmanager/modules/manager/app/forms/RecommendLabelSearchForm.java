package forms;

public class RecommendLabelSearchForm {
	Integer pageSize;

	Integer pageNum;

	Integer siteId;

	String cdevice;

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

	public String getCdevice() {
		return cdevice;
	}

	public void setCdevice(String cdevice) {
		this.cdevice = cdevice;
	}

	@Override
	public String toString() {
		return "CategoryLabelSearchForm [pageSize=" + pageSize + ", pageNum="
				+ pageNum + ", siteId=" + siteId + ", cdevice=" + cdevice + "]";
	}
}
