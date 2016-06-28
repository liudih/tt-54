package entity.loyalty;

import java.util.Date;

import services.loyalty.CouponService;

/**
 * 推广码实体类
 * @author lijun
 *
 */
public class PromoCode {
	private Integer id;
	private Integer websiteId;
	private Integer memberId;
	private String code;
	private Float parValue;
	private String currency;
	private String remark;
	private Date createDate;
	private Date endDate;
	private Integer status;
	private Integer ruleId;
	private String creater;
	private String ruleName;
	
	
	public String getStatusName(){
		if(CouponService.STATUS_AVAILABLE == this.status){
			return "可用";
		}else if(CouponService.STATUS_LOCKED == this.status){
			return "被锁定";
		}else if(CouponService.STATUS_BE_USED == this.status){
			return "已使用";
		}
		return "";
	}
	public String getRuleName() {
		return ruleName;
	}
	public void setRuleName(String ruleName) {
		this.ruleName = ruleName;
	}
	public String getCreater() {
		return creater;
	}
	public void setCreater(String creater) {
		this.creater = creater;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getWebsiteId() {
		return websiteId;
	}
	public void setWebsiteId(Integer websiteId) {
		this.websiteId = websiteId;
	}
	public Integer getMemberId() {
		return memberId;
	}
	public void setMemberId(Integer memberId) {
		this.memberId = memberId;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public Float getParValue() {
		return parValue;
	}
	public void setParValue(Float parValue) {
		this.parValue = parValue;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Integer getRuleId() {
		return ruleId;
	}
	public void setRuleId(Integer ruleId) {
		this.ruleId = ruleId;
	}
}
