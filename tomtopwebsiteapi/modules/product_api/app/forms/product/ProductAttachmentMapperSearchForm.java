package forms.product;

import services.base.utils.StringUtils;

public class ProductAttachmentMapperSearchForm {
	private Integer websiteId;

	private Integer languageId;

	private String sku;

	private String title;

	private Integer pageSize;

	private Integer pageNum;

	public Integer getWebsiteId() {
		return websiteId;
	}

	public void setWebsiteId(Integer websiteId) {
		this.websiteId = websiteId;
	}

	public Integer getLanguageId() {
		return languageId;
	}

	public void setLanguageId(Integer languageId) {
		this.languageId = languageId;
	}

	public String getSku() {
		if (StringUtils.isEmpty(sku)) {
			return null;
		}
		return sku;
	}
	
	public String getTitle() {
		if (StringUtils.isEmpty(title)) {
			return "";
		}
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	public Integer getPageSize() {
		return pageSize != null ? pageSize : 20;
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

}
