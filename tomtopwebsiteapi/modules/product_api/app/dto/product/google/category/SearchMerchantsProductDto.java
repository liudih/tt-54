package dto.product.google.category;

import java.io.Serializable;
import java.util.Date;

public class SearchMerchantsProductDto implements Serializable {

	private static final long serialVersionUID = -137084833171316970L;
	/**
	 * 
	 */
	private String csku;			//sku
	private String ctitle;			//名称
	private String cdescription;	//描述
	private Integer iwebsiteid;		//主站
	private String cchannel;		//--是否在线
	private String productprice;	//价格 需要加上货币符号，
	private String ctargetcountry;	//目标国家
	private String ccountrycurrency;//币种
	private String clanguage;		//目标语言
	private String googlecategory;	//--谷歌品类
	private String cpath;			//主站品类
	private String customLabel1;	//客户标签
	private String cstate;			//1：拉取记录状态，2：更新刊登产品状态， 3：主推产品  
	private String cresult;			//操作结果 success：成功   fail：失败
	private String cfaultreason;	//失败原因
	private String cnodeid;			//节点ID
	private Date dpulldate;			//拉去数据日期
	private Date dpushdate;			//推送更新数据日期
	private Date dcreatedate;		//创建日期
	private Date dsupdatedate;		//更新日期
	private Date deupdatedate;		//更新日期
	private Integer pageSize;		//分页大小
	private Integer page;			//当前页
	private Integer pageRange;		//分页控件间隔显示记录分页数
	
	private String extendf1;		//扩展字段
	public String getExtendf1() {
		return extendf1;
	}
	public void setExtendf1(String extendf1) {
		this.extendf1 = extendf1;
	}
	public Integer getPageSize() {
		return pageSize;
	}
	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}
	public Integer getPage() {
		return page;
	}
	public void setPage(Integer page) {
		this.page = page;
	}
	public Integer getPageRange() {
		return pageRange;
	}
	public void setPageRange(Integer pageRange) {
		this.pageRange = pageRange;
	}
	public String getCsku() {
		return csku;
	}
	public void setCsku(String csku) {
		this.csku = csku;
	}
	public String getCtitle() {
		return ctitle;
	}
	public void setCtitle(String ctitle) {
		this.ctitle = ctitle;
	}
	public String getCdescription() {
		return cdescription;
	}
	public void setCdescription(String cdescription) {
		this.cdescription = cdescription;
	}
	public Integer getIwebsiteid() {
		return iwebsiteid;
	}
	public void setIwebsiteid(Integer iwebsiteid) {
		this.iwebsiteid = iwebsiteid;
	}
	public String getCchannel() {
		return cchannel;
	}
	public void setCchannel(String cchannel) {
		this.cchannel = cchannel;
	}
	public String getProductprice() {
		return productprice;
	}
	public void setProductprice(String productprice) {
		this.productprice = productprice;
	}
	public String getCtargetcountry() {
		return ctargetcountry;
	}
	public void setCtargetcountry(String ctargetcountry) {
		this.ctargetcountry = ctargetcountry;
	}
	public String getCcountrycurrency() {
		return ccountrycurrency;
	}
	public void setCcountrycurrency(String ccountrycurrency) {
		this.ccountrycurrency = ccountrycurrency;
	}
	public String getClanguage() {
		return clanguage;
	}
	public void setClanguage(String clanguage) {
		this.clanguage = clanguage;
	}
	public String getGooglecategory() {
		return googlecategory;
	}
	public void setGooglecategory(String googlecategory) {
		this.googlecategory = googlecategory;
	}
	public String getCpath() {
		return cpath;
	}
	public void setCpath(String cpath) {
		this.cpath = cpath;
	}
	public String getCustomLabel1() {
		return customLabel1;
	}
	public void setCustomLabel1(String customLabel1) {
		this.customLabel1 = customLabel1;
	}
	public String getCstate() {
		return cstate;
	}
	public void setCstate(String cstate) {
		this.cstate = cstate;
	}
	public String getCresult() {
		return cresult;
	}
	public void setCresult(String cresult) {
		this.cresult = cresult;
	}
	public String getCfaultreason() {
		return cfaultreason;
	}
	public void setCfaultreason(String cfaultreason) {
		this.cfaultreason = cfaultreason;
	}
	public String getCnodeid() {
		return cnodeid;
	}
	public void setCnodeid(String cnodeid) {
		this.cnodeid = cnodeid;
	}
	public Date getDpulldate() {
		return dpulldate;
	}
	public void setDpulldate(Date dpulldate) {
		this.dpulldate = dpulldate;
	}
	public Date getDpushdate() {
		return dpushdate;
	}
	public void setDpushdate(Date dpushdate) {
		this.dpushdate = dpushdate;
	}
	public Date getDcreatedate() {
		return dcreatedate;
	}
	public void setDcreatedate(Date dcreatedate) {
		this.dcreatedate = dcreatedate;
	}
	public Date getDsupdatedate() {
		return dsupdatedate;
	}
	public void setDsupdatedate(Date dsupdatedate) {
		this.dsupdatedate = dsupdatedate;
	}
	public Date getDeupdatedate() {
		return deupdatedate;
	}
	public void setDeupdatedate(Date deupdatedate) {
		this.deupdatedate = deupdatedate;
	}
	@Override
	public String toString() {
		return "SearchMerchantsProductDto [csku=" + csku + ", ctitle=" + ctitle
				+ ", cdescription=" + cdescription + ", iwebsiteid="
				+ iwebsiteid + ", cchannel=" + cchannel + ", productprice="
				+ productprice + ", ctargetcountry=" + ctargetcountry
				+ ", ccountrycurrency=" + ccountrycurrency + ", clanguage="
				+ clanguage + ", googlecategory=" + googlecategory + ", cpath="
				+ cpath + ", customLabel1=" + customLabel1 + ", cstate="
				+ cstate + ", cresult=" + cresult + ", cfaultreason="
				+ cfaultreason + ", cnodeid=" + cnodeid + ", dpulldate="
				+ dpulldate + ", dpushdate=" + dpushdate + ", dcreatedate="
				+ dcreatedate + ", dsupdatedate=" + dsupdatedate
				+ ", deupdatedate=" + deupdatedate + ", pageSize=" + pageSize
				+ ", page=" + page + ", pageRange=" + pageRange + "]";
	}
	
	
}
