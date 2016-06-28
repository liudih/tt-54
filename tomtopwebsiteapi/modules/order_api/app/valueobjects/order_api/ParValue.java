package valueobjects.order_api;

import java.io.Serializable;

public class ParValue implements Serializable {
	private Double value;
	private String currency;

	public Double getValue() {
		return value;
	}

	public void setValue(Double value) {
		this.value = value;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}
}
