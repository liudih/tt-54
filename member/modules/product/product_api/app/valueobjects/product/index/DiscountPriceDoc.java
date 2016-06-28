package valueobjects.product.index;

import java.util.Date;

import valueobjects.price.Price;

public class DiscountPriceDoc {

	@MappingType(type = "long")
	Date fromDate;
	@MappingType(type = "long")
	Date toDate;
	@MappingType(type = "double")
	double price;
	@MappingType(type = "double")
	double discount;

	public DiscountPriceDoc(Price price, double discount) {
		this.discount = discount;
		this.fromDate = price.getValidFrom();
		this.toDate = price.getValidTo();
		this.price = price.getUnitPrice();
	}

	public DiscountPriceDoc(Date from, Date to, double price, double discount) {
		this.discount = discount;
		this.fromDate = from;
		this.toDate = to;
		this.price = price;
	}

	public Date getFromDate() {
		return fromDate;
	}

	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	public Date getToDate() {
		return toDate;
	}

	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public double getDiscount() {
		return discount;
	}

	public void setDiscount(double discount) {
		this.discount = discount;
	}
}
