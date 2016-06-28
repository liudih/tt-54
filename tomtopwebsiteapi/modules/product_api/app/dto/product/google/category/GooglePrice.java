package dto.product.google.category;

import java.io.Serializable;

public class GooglePrice implements Serializable {

	  /**
	 * 
	 */
	private static final long serialVersionUID = -125526343751482897L;
	private java.lang.String currency;
	  private java.lang.String value;
	public java.lang.String getCurrency() {
		return currency;
	}
	public void setCurrency(java.lang.String currency) {
		this.currency = currency;
	}
	public java.lang.String getValue() {
		return value;
	}
	public void setValue(java.lang.String value) {
		this.value = value;
	}

}
