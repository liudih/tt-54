package entity.loyalty;

import java.util.Date;

import enums.loyalty.coupon.manager.Status;
import enums.loyalty.coupon.manager.Type;

public class CouponValue {

	private int id;
	private String websiteId;
	private String email;
	private int ruleId;
	private String ruleName;
	private int codeId;
	private String code;
	private int type;
	private Type typeEnum;
	private int status;
	private Status statusEnum;
	private int creator;
	private String creatorName;
	private Date createDate;
	private int modifierId;
	private Date modifyDate;
	private String createDateStr;
	private int parentId;
	// 优惠券有效开始时间
	private Date validStartDate;
	private Date validEndDate;
	// 已经使用的次数
	private int usedtimes;
	// 可用次数
	private int times;
	// 订单id
	private int orderId;
	private String orderNumber;
	// 最低消费金额
	private Double minAmount;
	private Double par;
	// 折扣百分比
	private Double discount;
	// 优惠券类型(现金券 or 优惠券)
	private Integer valueType;
	// 有效天数
	private Integer validDays;
	// 币种
	private Integer currency;
	// 优惠券
	private String value;
	// 有效期类型
	private String timeType;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getWebsiteId() {
		return websiteId;
	}

	public void setWebsiteId(String websiteId) {
		this.websiteId = websiteId;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public int getRuleId() {
		return ruleId;
	}

	public void setRuleId(int ruleId) {
		this.ruleId = ruleId;
	}

	public String getRuleName() {
		return ruleName;
	}

	public void setRuleName(String ruleName) {
		this.ruleName = ruleName;
	}

	public int getCodeId() {
		return codeId;
	}

	public void setCodeId(int codeId) {
		this.codeId = codeId;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public Type getTypeEnum() {
		return typeEnum;
	}

	public void setTypeEnum(Type typeEnum) {
		this.typeEnum = typeEnum;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Status getStatusEnum() {
		return statusEnum;
	}

	public void setStatusEnum(Status statusEnum) {
		this.statusEnum = statusEnum;
	}

	public int getCreator() {
		return creator;
	}

	public void setCreator(int creator) {
		this.creator = creator;
	}

	public String getCreatorName() {
		return creatorName;
	}

	public void setCreatorName(String creatorName) {
		this.creatorName = creatorName;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public int getModifierId() {
		return modifierId;
	}

	public void setModifierId(int modifierId) {
		this.modifierId = modifierId;
	}

	public Date getModifyDate() {
		return modifyDate;
	}

	public void setModifyDate(Date modifyDate) {
		this.modifyDate = modifyDate;
	}

	public String getCreateDateStr() {
		return createDateStr;
	}

	public void setCreateDateStr(String createDateStr) {
		this.createDateStr = createDateStr;
	}

	public int getParentId() {
		return parentId;
	}

	public void setParentId(int parentId) {
		this.parentId = parentId;
	}

	public Date getValidStartDate() {
		return validStartDate;
	}

	public void setValidStartDate(Date validStartDate) {
		this.validStartDate = validStartDate;
	}

	public Date getValidEndDate() {
		return validEndDate;
	}

	public void setValidEndDate(Date validEndDate) {
		this.validEndDate = validEndDate;
	}

	public int getUsedtimes() {
		return usedtimes;
	}

	public void setUsedtimes(int usedtimes) {
		this.usedtimes = usedtimes;
	}

	public int getTimes() {
		return times;
	}

	public void setTimes(int times) {
		this.times = times;
	}

	public int getOrderId() {
		return orderId;
	}

	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}

	public String getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}

	public Double getMinAmount() {
		return minAmount;
	}

	public void setMinAmount(Double minAmount) {
		this.minAmount = minAmount;
	}

	public Double getPar() {
		return par;
	}

	public void setPar(Double par) {
		this.par = par;
	}

	public Double getDiscount() {
		return discount;
	}

	public void setDiscount(Double discount) {
		this.discount = discount;
	}

	public Integer getValueType() {
		return valueType;
	}

	public void setValueType(Integer valueType) {
		this.valueType = valueType;
	}

	public Integer getValidDays() {
		return validDays;
	}

	public void setValidDays(Integer validDays) {
		this.validDays = validDays;
	}

	public Integer getCurrency() {
		return currency;
	}

	public void setCurrency(Integer currency) {
		this.currency = currency;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getTimeType() {
		return timeType;
	}

	public void setTimeType(String timeType) {
		this.timeType = timeType;
	}

}
