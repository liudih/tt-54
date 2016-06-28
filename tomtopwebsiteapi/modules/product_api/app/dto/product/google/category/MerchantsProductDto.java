package dto.product.google.category;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

public class MerchantsProductDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1879650276745007229L;
	private Integer iid;			//iid
	private String clistingid;		//clistingid
	private String csku;			//sku
	private String ctitle;			//名称
	private String cdescription;	//描述
	private Integer iwebsiteid;		//主站
	private int ilanguage;			//主站语言
	private int icount;				//数量
	private String cchannel;		//--是否在线
	private String productprice;	//价格
	private String ctargetcountry;	//目标国家
	private String ccountrycurrency;//币种
	private String clanguage;		//目标语言
	private String googlecategory;	//--谷歌品类
	private int igooglecategoryid;	//--谷歌品类id
	private String cpath;			//主站品类
	private String cname;			//当前品类名
	private String customLabel1;	//客户标签
	private String cstate;			//1：拉取记录状态，2：更新刊登产品状态， 3：主推产品  4:产品下架
	private String cresult;			//操作结果 success：成功   fail：失败
	private String cfaultreason;	//失败原因
	private String createuser;		//创建者
	private String cnodedata;		//节点数据
	private String cnodeid;			//节点ID
	private Map<String,Object> attributes;//属性
	private double shippingprice;   //运费
	private Date dpulldate;			//拉去数据日期
	private Date dpushdate;			//推送更新数据日期
	
	private Date dcreatedate;		//创建日期
	private Date dupdatedate;		//更新日期
	
	public int getIgooglecategoryid() {
		return igooglecategoryid;
	}
	public void setIgooglecategoryid(int igooglecategoryid) {
		this.igooglecategoryid = igooglecategoryid;
	}
	public Map<String, Object> getAttributes() {
		return attributes;
	}
	public void setAttributes(Map<String, Object> attributes) {
		this.attributes = attributes;
	}
	public double getShippingprice() {
		return shippingprice;
	}
	public void setShippingprice(double shippingprice) {
		this.shippingprice = shippingprice;
	}
	public String getCustomLabel1() {
		return customLabel1;
	}
	public void setCustomLabel1(String customLabel1) {
		this.customLabel1 = customLabel1;
	}
	public String getCcountrycurrency() {
		return ccountrycurrency;
	}
	public void setCcountrycurrency(String ccountrycurrency) {
		this.ccountrycurrency = ccountrycurrency;
	}
	public String getCtargetcountry() {
		return ctargetcountry;
	}
	public void setCtargetcountry(String ctargetcountry) {
		this.ctargetcountry = ctargetcountry;
	}
	public String getGooglecategory() {
		return googlecategory;
	}
	public void setGooglecategory(String googlecategory) {
		this.googlecategory = googlecategory;
	}
	public String getCfaultreason() {
		return cfaultreason;
	}
	public void setCfaultreason(String cfaultreason) {
		this.cfaultreason = cfaultreason;
	}
	public int getIcount() {
		return icount;
	}
	public void setIcount(int icount) {
		this.icount = icount;
	}
	public String getCchannel() {
		return cchannel;
	}
	public String getClistingid() {
		return clistingid;
	}
	public void setClistingid(String clistingid) {
		this.clistingid = clistingid;
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
	public String getCresult() {
		return cresult;
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
	public void setCresult(String cresult) {
		this.cresult = cresult;
	}
	
	public String getCstate() {
		return cstate;
	}
	public void setCstate(String cstate) {
		this.cstate = cstate;
	}
	//更新的值
	public Integer getIid() {
		return iid;
	}
	
	public void setIid(Integer iid) {
		this.iid = iid;
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
	public int getIlanguage() {
		return ilanguage;
	}
	public void setIlanguage(int ilanguage) {
		this.ilanguage = ilanguage;
	}
	
	public String getClanguage() {
		return clanguage;
	}
	public void setClanguage(String clanguage) {
		this.clanguage = clanguage;
	}
	
	public String getCpath() {
		return cpath;
	}
	public void setCpath(String cpath) {
		this.cpath = cpath;
	}
	public String getCname() {
		return cname;
	}
	public void setCname(String cname) {
		this.cname = cname;
	}
	
	public String getCreateuser() {
		return createuser;
	}
	public void setCreateuser(String createuser) {
		this.createuser = createuser;
	}
	public String getCnodedata() {
		return cnodedata;
	}
	public void setCnodedata(String cnodedata) {
		this.cnodedata = cnodedata;
	}
	public String getCnodeid() {
		return cnodeid;
	}
	public void setCnodeid(String cnodeid) {
		this.cnodeid = cnodeid;
	}
	public Date getDcreatedate() {
		return dcreatedate;
	}
	public void setDcreatedate(Date dcreatedate) {
		this.dcreatedate = dcreatedate;
	}
	public Date getDupdatedate() {
		return dupdatedate;
	}
	public void setDupdatedate(Date dupdatedate) {
		this.dupdatedate = dupdatedate;
	}
	
}
