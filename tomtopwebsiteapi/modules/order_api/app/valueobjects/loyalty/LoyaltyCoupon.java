package valueobjects.loyalty;

import java.io.Serializable;

/**
 * 
 * @author xiaoch
 *
 */
public class LoyaltyCoupon implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// 优惠券类型
	private String couponType;

	private String code;

	// 现金券为金额，折扣券为折扣，不带单位
	private Double value;

	// 单位，现金券为币种，折扣券为百分比
	private String unit;

	// 最小消费金额
	private Double spendLimitValue;

	// 最初使用规则设置的币种
	private String spendLimitCurrency;

	private String startDate;

	private String endDate;

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public Double getSpendLimitValue() {
		return spendLimitValue;
	}

	public void setSpendLimitValue(Double spendLimitValue) {
		this.spendLimitValue = spendLimitValue;
	}

	public String getSpendLimitCurrency() {
		return spendLimitCurrency;
	}

	public void setSpendLimitCurrency(String spendLimitCurrency) {
		this.spendLimitCurrency = spendLimitCurrency;
	}

	public String getCouponType() {
		return couponType;
	}

	public void setCouponType(String couponType) {
		this.couponType = couponType;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Double getValue() {
		return value;
	}

	public void setValue(Double value) {
		this.value = value;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

}
