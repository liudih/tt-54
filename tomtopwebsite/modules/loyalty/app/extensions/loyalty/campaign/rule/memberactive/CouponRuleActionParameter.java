package extensions.loyalty.campaign.rule.memberactive;

import java.util.Date;
import java.util.List;

import org.json.simple.JSONObject;

import services.base.utils.StringUtils;
import services.campaign.IActionParameter;
import services.campaign.IActionRuleParameter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;

import enums.loyalty.coupon.manager.CouponRuleBack;
import enums.loyalty.coupon.manager.CouponRuleSelect;
import extensions.loyalty.campaign.coupon.CouponUseAction;

/**
 * 优惠券rule参数
 * 
 * @author lijun
 *
 */
public class CouponRuleActionParameter implements IActionRuleParameter,
		IActionParameter {
	public static final String ID = CouponUseAction.ID;
	// 数据库id
	private final int ruleId;
	// 有效时间类型(优惠券产生之日起有效天数 or 有效日期段)
	private CouponRuleSelect.TimeType timeType;
	// 优惠券类型(现金券 or 折扣券)
	private CouponRuleBack.CouponType type;
	// 时间类型
	// private String ctimetype;
	// 每笔订单最低消费金额
	private Double forderamountlimit;
	// 购物卷类型id
	// private Integer itype;
	// 币种
	private Integer ccurrency;
	// 面值
	private Double fcouponamount;
	// 创建时间
	private Date dcreatedate;
	// 使用期限开始时间
	private Date startdate;
	// 使用期限结束时间
	private Date enddate;
	// 折扣(单位：%)
	private Float fdiscount;
	// 有效期(单位：天)
	private Integer ivalidity;
	// 产品类型
	// private String cproducttype;
	// 排除的产品类型
	private List<String> excludeProductIds;
	// 排除的品类过滤ID
	private List<Integer> excludeCategoryIds;

	private String code;
	
	// 终端类型
	private List<String> useTerminal;
	
	// 允许使用的sku
	private List<String> skus;
	
	// 推广码允许使用次数,此属性为推广码专用
	private Integer ipromousetimes;
	
	public Integer getIpromousetimes() {
		return ipromousetimes;
	}


	public void setIpromousetimes(Integer ipromousetimes) {
		this.ipromousetimes = ipromousetimes;
	}


	public List<String> getSkus() {
		return skus;
	}


	public void setSkus(List<String> skus) {
		this.skus = skus;
	}

	public List<String> getUseTerminal() {
		return useTerminal;
	}


	public void setUseTerminal(List<String> useTerminal) {
		this.useTerminal = useTerminal;
	}


	public CouponRuleActionParameter(int ruleId) {
		this.ruleId = ruleId;
	}

	
	public int getRuleId() {
		return ruleId;
	}


	@Override
	public String getActionRuleId() {
		return ID;
	}

	@Override
	public String getActionId() {
		return ID;
	}

	public void setCproducttype(String cproducttype) {
		if (StringUtils.notEmpty(cproducttype)) {
			this.excludeProductIds = Lists.newArrayList(cproducttype.split(","));
		}
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Double getForderamountlimit() {
		return forderamountlimit;
	}

	public void setForderamountlimit(Double forderamountlimit) {
		this.forderamountlimit = forderamountlimit;
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

	public Float getFdiscount() {
		return fdiscount;
	}

	public void setFdiscount(Float fdiscount) {
		this.fdiscount = fdiscount;
	}

	public Integer getIvalidity() {
		return ivalidity;
	}

	public void setIvalidity(Integer ivalidity) {
		this.ivalidity = ivalidity;
	}

	public List<String> getExcludeProductIds() {
		return excludeProductIds;
	}

	public List<Integer> getExcludeCategoryIds() {
		return excludeCategoryIds;
	}

	public void setExcludeCategoryIds(List<Integer> excludeCategoryIds) {
		this.excludeCategoryIds = excludeCategoryIds;
	}

	public CouponRuleSelect.TimeType getTimeType() {
		return timeType;
	}

	public CouponRuleBack.CouponType getType() {
		return type;
	}

	public void setItype(Integer itype) {
		this.type = CouponRuleBack.CouponType.get(itype);
	}

	public void setCtimetype(String ctimetype) {
		this.timeType = CouponRuleSelect.TimeType.get(ctimetype);
	}

	@Override
	public String toString() {
		ObjectMapper objectMapper = new ObjectMapper();
		JSONObject json = objectMapper.convertValue(this, JSONObject.class);
		return json.toString();
	}
}
