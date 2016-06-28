package valueobjects.price;

import java.io.Serializable;
import java.util.Date;

import play.Logger;
import play.libs.F;
import valueobjects.product.spec.IProductSpec;

/**
 * <p>
 * 价格：根据 Listing 和 数量 判断出来的具体价格（交易用）。如果需要显示价格的可能性（不是用来做交易的：如批发价的阶梯价格），需要自行计算。
 * 
 * <p>
 * 比较关键的概念：
 * 
 * <ul>
 * <li>是否容许亏本（lossAllowed）字段，会让 UnitPrice 字段如果低于 UnitCost 的话，会自行调整成为 UnitCost。
 * <li>兑换率：相对美金而言的兑换率（XXX 但是应用PriceBuilder.change()方法就被设成1）
 * </ul>
 * 
 * @author kmtong
 *
 */
public class Price implements IPrice, Serializable {

	private static final long serialVersionUID = 2867481967520752597L;

	/**
	 * 商品标识＋数量
	 */
	final IProductSpec spec;

	/**
	 * 基础单价（基于制定的货币）
	 */
	double unitBasePrice;

	/**
	 * 折扣后的单价（基于制定的货币）
	 */
	double unitPrice;

	/**
	 * 成本价
	 */
	double unitCost;

	/**
	 * 折扣 (0.0 - 1.0] NULL表示没有折扣
	 * 
	 */
	Double discount;

	/**
	 * 价格有效期
	 */
	Date validFrom;

	/**
	 * 价格有效期
	 */
	Date validTo;

	/**
	 * 货币类型
	 */
	String currency;

	/**
	 * 货币使用的符号
	 */
	String symbol = "$";

	/**
	 * 兑换率
	 */
	double rate = 1.0;

	boolean lossAllowed = false;

	public Price(IProductSpec spec) {
		if (spec == null) {
			throw new RuntimeException("IProductSpec cannot be null");
		}
		this.spec = spec;
	}

	public IProductSpec getSpec() {
		return spec;
	}

	/**
	 * off 折扣
	 * 
	 * @return
	 */
	public Double getDiscount() {
		return discount;
	}

	/**
	 * 原价
	 * 
	 * @return
	 */
	public Double getUnitBasePrice() {
		return unitBasePrice;
	}

	/**
	 * 原单价
	 * 
	 * @param basePrice
	 */
	public void setUnitBasePrice(double unitBasePrice) {
		this.unitBasePrice = unitBasePrice;
	}

	public Integer getQuantity() {
		return spec.getQty();
	}

	public void setQuantity(Integer quantity) {
		// this.quantity = quantity;
		throw new UnsupportedOperationException(
				"Cannot set quantity, use constructor");
	}

	public String getListingId() {
		return spec.getListingID();
	}

	public void setListingId(String listingId) {
		// this.listingId = listingId;
		throw new UnsupportedOperationException(
				"Cannot set listing ID now, use constructor");
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public double getRate() {
		return rate;
	}

	public void setRate(double rate) {
		this.rate = rate;
	}

	public double getUnitPrice() {
		if (lossAllowed) {
			return unitPrice;
		} else {
			return unitPrice > unitCost ? unitPrice : unitCost;
		}
	}

	public void setUnitPrice(double unitPrice) {
		this.unitPrice = unitPrice;
		if (!lossAllowed && unitPrice < unitCost) {
			Logger.warn(
					"UnitPrice {} set lower than UnitCost {}, adjusted accordingly",
					unitPrice, unitCost);
		}
	}

	public double getUnitCost() {
		return unitCost;
	}

	public void setUnitCost(double unitCost) {
		this.unitCost = unitCost;
	}

	public Date getValidFrom() {
		return validFrom;
	}

	public void setValidFrom(Date validFrom) {
		this.validFrom = validFrom;
	}

	public Date getValidTo() {
		return validTo;
	}

	public void setValidTo(Date validTo) {
		this.validTo = validTo;
	}

	public void setDiscount(Double discount) {
		this.discount = discount;
	}

	public double getPrice() {
		double up = this.getUnitPrice();
		//add by lijun
		if("JPY".equals(this.currency)){
			up = Math.round(up);
		}
		return up * getQuantity();
	}

	public F.Option<Double> getDiscountOption() {
		if (discount != null) {
			return F.Option.Some(discount);
		}
		return F.Option.<Double> None();
	}

	public F.Option<Date> getValidFromOption() {
		if (validFrom != null) {
			return F.Option.Some(validFrom);
		}
		return F.Option.<Date> None();
	}

	public F.Option<Date> getValidToOption() {
		if (validTo != null) {
			return F.Option.Some(validTo);
		}
		return F.Option.<Date> None();
	}

	public boolean isDiscounted() {
		if (discount != null && discount > 0 && discount <= 1
				&& this.unitPrice < this.unitBasePrice) {
			return true;
		} else {
			return false;
		}

	}

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public boolean isLossAllowed() {
		return lossAllowed;
	}

	public void setLossAllowed(boolean lossAllowed) {
		this.lossAllowed = lossAllowed;
	}

	@Override
	public String toString() {
		return "Price [spec=" + spec + ", unitBasePrice=" + unitBasePrice
				+ ", unitPrice=" + unitPrice + ", currency=" + currency + "]";
	}

}
