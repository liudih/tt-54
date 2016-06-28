package entity.loyalty;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import enums.loyalty.coupon.manager.CouponRuleBack;
import enums.loyalty.coupon.manager.Status;
import enums.loyalty.coupon.manager.Type;
import dto.Currency;

public class Coupon implements Serializable{
	
	private static final long serialVersionUID = 1L;
	public static final String PATTERN = "yyyy-MM-dd HH:mm:ss";
	private int id;
	private int websiteId;
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
	//折扣百分比
	private Double discount;
	//优惠券类型(现金券 or 优惠券)
	private Integer valueType;
	//有效天数
	private Integer validDays;
	//币种
	private Integer currency;
	//优惠券
	private String value;
	//有效期类型
	private String timeType;
	
	private Currency co;
	
	/**
	 * 是否现金券
	 * @return
	 */
	public boolean isCash(){
		return CouponRuleBack.CouponType.CASH
		.getCode() == this.getValueType();
	}
	
	public Currency getCo() {
		return co;
	}

	public void setCo(Currency co) {
		this.co = co;
	}

	public String getTimeType() {
		return timeType;
	}

	public void setTimeType(String timeType) {
		this.timeType = timeType;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
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

	public String getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}

	public int getOrderId() {
		return orderId;
	}

	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}

	public int getTimes() {
		return times;
	}

	public void setTimes(int times) {
		this.times = times;
	}

	public int getUsedtimes() {
		return usedtimes;
	}

	public void setUsedtimes(int usedtimes) {
		this.usedtimes = usedtimes;
	}

	public Date getValidStartDate() {
		return validStartDate;
	}

	public String getValidStartDateStr() {
		return this.getValidStartDate(PATTERN);
	}

	public String getValidStartDate(String pattern) {
		if (this.validStartDate == null) {
			return "";
		}
		String result = null;
		try {
			SimpleDateFormat formater = new SimpleDateFormat(pattern);
			result = formater.format(this.validStartDate);
		} catch (Exception e) {
			SimpleDateFormat formater = new SimpleDateFormat(PATTERN);
			result = formater.format(this.validStartDate);
		}
		return result;
	}

	public void setValidStartDate(Date validStartDate) {
		this.validStartDate = validStartDate;
	}

	public Date getValidEndDate() {
		return validEndDate;
	}

	public String getValidEndDateStr() {
		return this.getValidEndDate(PATTERN);
	}

	public String getValidEndDate(String pattern) {
		if (this.validEndDate == null) {
			return "";
		}
		String result = null;
		try {
			SimpleDateFormat formater = new SimpleDateFormat(pattern);
			result = formater.format(this.validEndDate);
		} catch (Exception e) {
			SimpleDateFormat formater = new SimpleDateFormat(PATTERN);
			result = formater.format(this.validEndDate);
		}
		return result;
	}

	public void setValidEndDate(Date validEndDate) {
		this.validEndDate = validEndDate;
	}

	public int getParentId() {
		return parentId;
	}

	public void setParentId(int parentId) {
		this.parentId = parentId;
	}

	public String getCreatorName() {
		return creatorName;
	}

	public void setCreatorName(String creatorName) {
		this.creatorName = creatorName;
	}

	public Type getTypeEnum() {
		return typeEnum;
	}

	public Status getStatusEnum() {
		return statusEnum;
	}

	public String getRuleName() {
		return ruleName;
	}

	public void setRuleName(String ruleName) {
		this.ruleName = ruleName;
	}

	public String getCreateDateStr() {
		return createDateStr;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getWebsiteId() {
		return websiteId;
	}

	public void setWebsiteId(int websiteId) {
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
		this.typeEnum = Type.getType(type);
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
		this.statusEnum = Status.getStatus(status);
	}

	public int getCreator() {
		return creator;
	}

	public void setCreator(int creator) {
		this.creator = creator;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
		try {
			SimpleDateFormat formater = new SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss");
			this.createDateStr = formater.format(createDate);
		} catch (Exception e) {

		}

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

}
