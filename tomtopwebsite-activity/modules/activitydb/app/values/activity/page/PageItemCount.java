package values.activity.page;

public class PageItemCount {

	public Integer pageId;
	public Integer pageItemId;
	public Integer itemCount;

	public Integer getPageId() {
		return pageId;
	}

	public void setPageId(Integer pageId) {
		this.pageId = pageId;
	}

	public Integer getPageItemId() {
		return pageItemId;
	}

	public void setPageItemId(Integer pageItemId) {
		this.pageItemId = pageItemId;
	}

	public Integer getItemCount() {
		return itemCount == null ? 0 : itemCount;
	}

	public void setItemCount(Integer itemCount) {
		this.itemCount = itemCount;
	}

}
