package entity.loyalty;

import java.util.Date;
import java.util.List;

import org.json.simple.JSONObject;

import com.fasterxml.jackson.databind.ObjectMapper;

public class CouponRule {

	private Integer iid;

	private String cname;
	// 购物卷类型id
	private Integer itype;

	private String cwebsiteid;

	// 币种
	private Integer ccurrency;

	// 面值
	private Double fcouponamount;

	// 每笔订单最低消费金额
	private Double forderamountlimit;

	// 状态(0 禁用,1 可用)
	private Integer istatus;

	// 描述
	private String cdescription;

	// 创建时间
	private Date dcreatedate;

	// 使用期限开始时间
	private Date startdate;

	// 使用期限结束时间
	private Date enddate;

	// 创建人ID
	private Integer icreator;

	// 登陆终端类型
	private String iloginterminal;

	// 折扣(单位：%)
	private Float fdiscount;

	// 时间类型
	private String ctimetype;

	// 有效期(单位：天)
	private Integer ivalidity;

	// 产品类型
	private String cproducttype;

	// 排除的品类过滤ID
	private List<Integer> excludeCategoryIds;

	// 允许使用的终端
	private String cuseterminal;

	// 指定sku
	private String csku;

	// 推广码允许使用次数,此属性为推广码专用
	private Integer ipromousetimes;

	public Integer getIpromousetimes() {
		return ipromousetimes;
	}

	public void setIpromousetimes(Integer ipromousetimes) {
		this.ipromousetimes = ipromousetimes;
	}

	public String getCsku() {
		return csku;
	}

	public void setCsku(String csku) {
		this.csku = csku;
	}

	public String getCuseterminal() {
		return cuseterminal;
	}

	public void setCuseterminal(String cuseterminal) {
		this.cuseterminal = cuseterminal;
	}

	public String getCwebsiteid() {
		return cwebsiteid;
	}

	public void setCwebsiteid(String cwebsiteid) {
		this.cwebsiteid = cwebsiteid;
	}

	public List<Integer> getExcludeCategoryIds() {
		return excludeCategoryIds;
	}

	public void setExcludeCategoryIds(List<Integer> excludeCategoryIds) {
		this.excludeCategoryIds = excludeCategoryIds;
	}

	public String getCproducttype() {
		return cproducttype;
	}

	public void setCproducttype(String cproducttype) {
		this.cproducttype = cproducttype;
	}

	public String getCname() {
		return cname;
	}

	public void setCname(String cname) {
		this.cname = cname;
	}

	public Integer getIid() {
		return iid;
	}

	public void setIid(Integer iid) {
		this.iid = iid;
	}

	public Integer getItype() {
		return itype;
	}

	public void setItype(Integer itype) {
		this.itype = itype;
	}

	public Integer getCcurrency() {
		return ccurrency;
	}

	public void setCcurrency(Integer ccurrency) {
		this.ccurrency = ccurrency;
	}

	public Double getFcouponamount() {
		return fcouponamount;
	}

	public void setFcouponamount(Double fcouponamount) {
		this.fcouponamount = fcouponamount;
	}

	public Double getForderamountlimit() {
		return forderamountlimit;
	}

	public void setForderamountlimit(Double forderamountlimit) {
		this.forderamountlimit = forderamountlimit;
	}

	public Integer getIstatus() {
		return istatus;
	}

	public void setIstatus(Integer istatus) {
		this.istatus = istatus;
	}

	public String getCdescription() {
		return cdescription;
	}

	public void setCdescription(String cdescription) {
		this.cdescription = cdescription;
	}

	public Date getDcreatedate() {
		return dcreatedate;
	}

	public void setDcreatedate(Date dcreatedate) {
		this.dcreatedate = dcreatedate;
	}

	public Date getStartdate() {
		return startdate;
	}

	public void setStartdate(Date startdate) {
		this.startdate = startdate;
	}

	public Date getEnddate() {
		return enddate;
	}

	public void setEnddate(Date enddate) {
		this.enddate = enddate;
	}

	public Integer getIcreator() {
		return icreator;
	}

	public void setIcreator(Integer icreator) {
		this.icreator = icreator;
	}

	public String getIloginterminal() {
		return iloginterminal;
	}

	public void setIloginterminal(String iloginterminal) {
		this.iloginterminal = iloginterminal;
	}

	public Float getFdiscount() {
		return fdiscount;
	}

	public void setFdiscount(Float fdiscount) {
		this.fdiscount = fdiscount;
	}

	public String getCtimetype() {
		return ctimetype;
	}

	public void setCtimetype(String ctimetype) {
		this.ctimetype = ctimetype;
	}

	public Integer getIvalidity() {
		return ivalidity;
	}

	public void setIvalidity(Integer ivalidity) {
		this.ivalidity = ivalidity;
	}

	@Override
	public String toString() {
		ObjectMapper objectMapper = new ObjectMapper();
		JSONObject json = objectMapper.convertValue(this, JSONObject.class);
		return json.toString();
	}

}
