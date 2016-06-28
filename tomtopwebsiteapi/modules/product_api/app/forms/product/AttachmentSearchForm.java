package forms.product;

import services.base.utils.StringUtils;

public class AttachmentSearchForm {
	private String ctype;

	private String cfilename;

	private String cpath;

	private Integer pageSize;

	private Integer pageNum;

	public String getCtype() {
		if (StringUtils.isEmpty(ctype)) {
			return null;
		}
		return ctype;
	}

	public void setCtype(String ctype) {
		this.ctype = ctype;
	}

	public String getCfilename() {
		if (StringUtils.isEmpty(cfilename)) {
			return null;
		}
		return cfilename;
	}

	public void setCfilename(String cfilename) {
		this.cfilename = cfilename;
	}

	public String getCpath() {
		if (StringUtils.isEmpty(cpath)) {
			return null;
		}
		return cpath;
	}

	public void setCpath(String cpath) {
		this.cpath = cpath;
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
