package forms.img;

import services.base.utils.StringUtils;

public class ImgPageForm {
	private String path;
	
	private String contentType;
	
	private Integer pageSize;
	
	private Integer pageNum;

	public String getPath() {
		if (StringUtils.isEmpty(path)) {
			return null;
		}
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getContentType() {
		if (StringUtils.isEmpty(contentType)) {
			return null;
		}
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType; 
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

	@Override
	public String toString() {
		return "ImgPageForm [path=" + path + ", contentType=" + contentType
				+ ", pageSize=" + pageSize + ", pageNum=" + pageNum + "]";
	}
	
	
}
