package valueobjects.loyalty;

import java.io.Serializable;

import dto.Currency;

/**
 * 
 * @author lijun
 *
 */
public class Coupon implements Serializable{
	
	private static final long serialVersionUID = 1L;
	// 是否是现金券
	private boolean isCash;
	// 现金金额
	private double amount;
	// 折扣百分比
	private double percent;
	// 优惠券code
	private String code;
	//币种
	private Currency currency;

	
	public Currency getCurrency() {
		return currency;
	}

	public void setCurrency(Currency currency) {
		this.currency = currency;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public boolean isCash() {
		return isCash;
	}

	public void setCash(boolean isCash) {
		this.isCash = isCash;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public double getPercent() {
		return percent;
	}

	public void setPercent(double percent) {
		this.percent = percent;
	}

}
