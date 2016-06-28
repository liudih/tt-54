package dto;

import java.util.Date;

/**
 * 首页基本信息管理实体类
 * @author guozy
 *
 */
public class Seo {
	
	//首页编号id
	private Integer iid;
	//站点编号
	private Integer iwebsiteid;
	//语言编号
	private Integer ilanguageid;
	//标题
	private String ctitle;
	//关键字
	private String ckeywords;
	//类型
	private String ctype;
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
	public Integer getIwebsiteid() {
		return iwebsiteid;
	}
	public void setIwebsiteid(Integer iwebsiteid) {
		this.iwebsiteid = iwebsiteid;
	}
	public Integer getIlanguageid() {
		return ilanguageid;
	}
	public void setIlanguageid(Integer ilanguageid) {
		this.ilanguageid = ilanguageid;
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
	public String getCtype() {
		return ctype;
	}
	public void setCtype(String ctype) {
		this.ctype = ctype;
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
	@Override
	public String toString() {
		return "HomeManager [iid=" + iid + ", iwebsiteid=" + iwebsiteid
				+ ", ilanguageid=" + ilanguageid + ", ctitle=" + ctitle
				+ ", ckeywords=" + ckeywords + ", ctype=" + ctype
				+ ", cdescription=" + cdescription + ", ccreatename="
				+ ccreatename + ", dcreatedate=" + dcreatedate
				+ ", cmodifiedname=" + cmodifiedname + ", dmodifieddate="
				+ dmodifieddate + "]";
	}
	
	
}
