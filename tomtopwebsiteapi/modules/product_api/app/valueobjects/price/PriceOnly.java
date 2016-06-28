package valueobjects.price;

import java.io.Serializable;

public class PriceOnly implements IPrice, Serializable {
	private static final long serialVersionUID = 1L;
	final String currency;
	final double price;

	public PriceOnly(String currency, double price) {
		super();
		this.currency = currency;
		this.price = price;
	}

	public String getCurrency() {
		return currency;
	}

	public double getPrice() {
		return price;
	}

}
