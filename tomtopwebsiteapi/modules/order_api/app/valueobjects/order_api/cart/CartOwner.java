package valueobjects.order_api.cart;

import java.io.Serializable;

public class CartOwner implements Serializable {
	private static final long serialVersionUID = 1L;
	final String email;
	final String ltc;

	public CartOwner(String email, String ltc) {
		this.email = email;
		this.ltc = ltc;
	}

	public String getEmail() {
		return email;
	}

	public String getLtc() {
		return ltc;
	}

	public boolean isMemberOwned() {
		return (email != null);
	}
}
