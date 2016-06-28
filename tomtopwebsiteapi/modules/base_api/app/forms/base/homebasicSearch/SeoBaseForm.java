package forms.base.homebasicSearch;

import java.util.Date;

/**
 * 首页基本信息分页的基础类
 * @author Administrator
 *
 */
public class SeoBaseForm {
	private static final long serialVersionUID = 1L;
	private Integer pageSize;
	private Integer pageNum;
	private Integer iwebsiteid;
	private String ctype;
	private Integer ilanguageid;
	//首页编号id
	private Integer iid;
	//标题
	private String ctitle;
	//关键字
	private String ckeywords;
	//描述
	private String cdescription;
	//创建人
	private String ccreatename;
	//创建时间
	private Date dcreatedate;
	//修改人
	private String cmodifiedname;
	//修改时间
	private Date dmodifieddate;
	
	public Integer getIid() {
		return iid;
	}
	public void setIid(Integer iid) {
		this.iid = iid;
	}
	
	public String getCtitle() {
		return ctitle;
	}
	public void setCtitle(String ctitle) {
		this.ctitle = ctitle;
	}
	public String getCkeywords() {
		return ckeywords;
	}
	public void setCkeywords(String ckeywords) {
		this.ckeywords = ckeywords;
	}

	public String getCdescription() {
		return cdescription;
	}
	public void setCdescription(String cdescription) {
		this.cdescription = cdescription;
	}
	public String getCcreatename() {
		return ccreatename;
	}
	public void setCcreatename(String ccreatename) {
		this.ccreatename = ccreatename;
	}
	public Date getDcreatedate() {
		return dcreatedate;
	}
	public void setDcreatedate(Date dcreatedate) {
		this.dcreatedate = dcreatedate;
	}
	public String getCmodifiedname() {
		return cmodifiedname;
	}
	public void setCmodifiedname(String cmodifiedname) {
		this.cmodifiedname = cmodifiedname;
	}
	public Date getDmodifieddate() {
		return dmodifieddate;
	}
	public void setDmodifieddate(Date dmodifieddate) {
		this.dmodifieddate = dmodifieddate;
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

	public Integer getIwebsiteid() {

		return iwebsiteid;
	}

	public void setIwebsiteid(Integer iwebsiteid) {
		this.iwebsiteid = iwebsiteid;
	}

	public String getCtype() {
		if(ctype==null||"".equals(ctype)){
			return null;
		}
		return ctype;
	}

	public void setCtype(String ctype) {
		this.ctype = ctype;
	}

	public Integer getIlanguageid() {
		return ilanguageid;
	}

	public void setIlanguageid(Integer ilanguageid) {
		this.ilanguageid = ilanguageid;
	}
	
}
