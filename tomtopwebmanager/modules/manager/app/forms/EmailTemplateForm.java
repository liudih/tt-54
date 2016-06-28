package forms;

public class EmailTemplateForm {

	private Integer iid;
	private Integer iwebsiteid;
	private Integer ilanguage;
	private String ctype;
	public String ctitle;
	private String ccontent;
	private Integer pageNo;
	private Integer totalPage;

	public Integer getIid() {
		return iid;
	}

	public Integer getIwebsiteid() {
		return iwebsiteid;
	}

	public String getCtype() {
		return ctype;
	}

	public String getCtitle() {
		return ctitle;
	}

	public String getCcontent() {
		return ccontent;
	}

	public void setIid(Integer iid) {
		this.iid = iid;
	}

	public void setIwebsiteid(Integer iwebsiteid) {
		this.iwebsiteid = iwebsiteid;
	}

	public void setCtype(String ctype) {
		this.ctype = ctype;
	}

	public void setCtitle(String ctitle) {
		this.ctitle = ctitle;
	}

	public void setCcontent(String ccontent) {
		this.ccontent = ccontent;
	}

	public Integer getPageNo() {
		return pageNo;
	}

	public void setPageNo(Integer pageNo) {
		this.pageNo = pageNo;
	}

	public Integer getTotalPage() {
		return totalPage;
	}

	public void setTotalPage(Integer totalPage) {
		this.totalPage = totalPage;
	}

	public Integer getIlanguage() {
		return ilanguage;
	}

	public void setIlanguage(Integer ilanguage) {
		this.ilanguage = ilanguage;
	}

}
