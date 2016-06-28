package valueobjects.cart;

import java.io.Serializable;
import java.util.Date;

public class Price implements Serializable {

	private static final long serialVersionUID = 2867481967520752597L;

	double unitBasePrice;
	
	double unitPrice;
	
	double price;

	Double discount;

	/**
	 * 价格有效期
	 */
	Date validFrom;

	Date validTo;

	String currency;

	String symbol = "US$";

	boolean isDiscounted;

	public double getUnitBasePrice() {
		return unitBasePrice;
	}

	public void setUnitBasePrice(double unitBasePrice) {
		this.unitBasePrice = unitBasePrice;
	}

	public double getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(double unitPrice) {
		this.unitPrice = unitPrice;
	}

	public Double getDiscount() {
		return discount;
	}

	public void setDiscount(Double discount) {
		this.discount = discount;
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

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public boolean isDiscounted() {
		return isDiscounted;
	}

	public void setDiscounted(boolean isDiscounted) {
		this.isDiscounted = isDiscounted;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}
}
