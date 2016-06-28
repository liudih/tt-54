package valueobjects.product;

import java.util.Date;

import valueobjects.product.spec.IProductSpec;
import valueobjects.product.spec.SingleProductSpec;

public class PriceExtra {
	SingleProductSpecExtra spec;
	double unitBasePrice;
	double unitPrice;
	double unitCost;
	Double discount;
	Date validFrom;
	Date validTo;
	String currency;
	String symbol = "$";
	boolean discounted;
	double rate = 1.0D;
	String listingId;
	double price;
	Integer quantity;

	boolean lossAllowed = false;

	public SingleProductSpecExtra getSpec() {
		return spec;
	}

	public void setSpec(SingleProductSpecExtra spec) {
		this.spec = spec;
	}

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

	public double getUnitCost() {
		return unitCost;
	}

	public void setUnitCost(double unitCost) {
		this.unitCost = unitCost;
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

	public double getRate() {
		return rate;
	}

	public void setRate(double rate) {
		this.rate = rate;
	}

	public boolean isLossAllowed() {
		return lossAllowed;
	}

	public void setLossAllowed(boolean lossAllowed) {
		this.lossAllowed = lossAllowed;
	}

	public boolean isDiscounted() {
		return discounted;
	}

	public void setDiscounted(boolean discounted) {
		this.discounted = discounted;
	}

	public String getListingId() {
		return listingId;
	}

	public void setListingId(String listingId) {
		this.listingId = listingId;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

}
